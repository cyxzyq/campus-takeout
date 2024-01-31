package com.sky.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.sky.constant.MessageConstant;
import com.sky.dto.*;
import com.sky.entity.AddressBook;
import com.sky.entity.OrderDetail;
import com.sky.entity.Orders;
import com.sky.entity.ShoppingCart;
import com.sky.exception.AddressBookBusinessException;
import com.sky.exception.OrderBusinessException;
import com.sky.interceptor.JwtTokenUserInterceptor;
import com.sky.mapper.AddressBookMapper;
import com.sky.mapper.OrderMapper;
import com.sky.mapper.ShoppingCartMapper;
import com.sky.result.PageResult;
import com.sky.service.OrderService;
import com.sky.vo.OrderStatisticsVO;
import com.sky.vo.OrderSubmitVO;
import com.sky.vo.OrderVO;
import com.sky.websocket.WebSocketServer;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

//订单
@Service
public class OrderServiceImpl implements OrderService {

    @Autowired
    private OrderMapper orderMapper;
    @Autowired
    private AddressBookMapper addressBookMapper;
    @Autowired
    private ShoppingCartMapper shoppingCartMapper;
    @Autowired
    private WebSocketServer webSocketServer;

    //用户下单
    @Override
    @Transactional
    public OrderSubmitVO submit(OrdersSubmitDTO ordersSubmitDTO) {
        //判断用户是否填写地址
        AddressBook addressBook = addressBookMapper.listAddressBook(ordersSubmitDTO.getAddressBookId());
        if (addressBook == null) {
            throw new AddressBookBusinessException(MessageConstant.ADDRESS_BOOK_IS_NULL);
        }
        //判断用户购物车内是否有商品
        ShoppingCart s = new ShoppingCart();
        s.setUserId(JwtTokenUserInterceptor.threadLocal.get());
        List<ShoppingCart> shoppingCart = shoppingCartMapper.findShoppingCart(s);
        if (shoppingCart == null && shoppingCart.size() == 0) {
            throw new AddressBookBusinessException(MessageConstant.SHOPPING_CART_IS_NULL);
        }

        //向订单表插入数据
        Orders orders = new Orders();
        BeanUtils.copyProperties(ordersSubmitDTO, orders);
        orders.setNumber(String.valueOf(System.currentTimeMillis()));
        orders.setStatus(Orders.PENDING_PAYMENT);
        orders.setUserId(JwtTokenUserInterceptor.threadLocal.get());
        orders.setOrderTime(LocalDateTime.now());
        orders.setPayStatus(Orders.UN_PAID);
        orders.setConsignee(addressBook.getConsignee());
        orders.setPhone(addressBook.getPhone());
        orders.setAddress(addressBook.getDetail());
        orderMapper.add(orders);

        List<OrderDetail> orderDetailList = new ArrayList<>();
        //向订单明细表插入n条数据
        for (ShoppingCart cart : shoppingCart) {
            OrderDetail orderDetail = new OrderDetail();
            BeanUtils.copyProperties(cart, orderDetail);
            orderDetail.setOrderId(orders.getId());
            orderDetailList.add(orderDetail);
        }
        orderMapper.addDetail(orderDetailList);

        //清空购物车数据
        shoppingCartMapper.clean(s);

        //封装返回数据
        OrderSubmitVO orderSubmitVO = new OrderSubmitVO();
        orderSubmitVO.setId(orders.getId());
        orderSubmitVO.setOrderTime(orders.getOrderTime());
        orderSubmitVO.setOrderAmount(orders.getAmount());
        orderSubmitVO.setOrderNumber(orders.getNumber());
        return orderSubmitVO;
    }

    //查询订单详情
    @Override
    public OrderVO orderDetail(Long id) {
        //根据订单id查询订单
        Orders orders = orderMapper.getById(id);
        //根据订单id查询订单详情表
        List<OrderDetail> orderDetails = orderMapper.getOrderDetailById(id);
        //封装返回数据
        OrderVO orderVO = new OrderVO();
        BeanUtils.copyProperties(orders, orderVO);
        orderVO.setOrderDetailList(orderDetails);
        return orderVO;
    }

    //查询历史订单
    @Override
    public PageResult page(Integer page, Integer pageSize, Integer status) {
        PageHelper.startPage(page, pageSize);
        //查询订单信息
        Page<Orders> ordersPage = orderMapper.getByUserId(JwtTokenUserInterceptor.threadLocal.get(), status);
        List<OrderVO> orderVOList = new ArrayList<>();
        if (ordersPage != null && ordersPage.getTotal() > 0) {
            for (Orders orders : ordersPage) {
                //查询订单详情
                List<OrderDetail> orderDetailById = orderMapper.getOrderDetailById(orders.getId());
                //封装返回数据
                OrderVO orderVO = new OrderVO();
                BeanUtils.copyProperties(orders, orderVO);
                orderVO.setOrderDetailList(orderDetailById);
                orderVOList.add(orderVO);
            }
        }
        return new PageResult(ordersPage.getTotal(), orderVOList);
    }

    //取消订单
    @Transactional
    @Override
    public void cancel(Long id) {
        //根据id查询订单
        Orders orders = orderMapper.getById(id);
        //检验订单是否存在
        if (orders == null) {
            throw new OrderBusinessException(MessageConstant.ORDER_NOT_FOUND);
        }
        //订单状态 1待付款 2待接单 3已接单 4派送中 5已完成 6已取消 7退款
        if (orders.getStatus() > 2) {
            throw new OrderBusinessException(MessageConstant.ORDER_STATUS_ERROR);
        }
        //若订单状态为待接单，则退款
        if (orders.getStatus().equals(Orders.TO_BE_CONFIRMED)) {
            Orders orders1 = new Orders();
            orders1.setId(orders.getId());
            orders1.setPayStatus(Orders.REFUND);
            //将订单支付状态设置为退款 payStatus=2
            orderMapper.update(orders1);
        }
        //更新订单状态，取消原因，取消时间
        Orders orders2 = new Orders();
        orders2.setId(orders.getId());
        orders2.setStatus(Orders.CANCELLED);
        orders2.setCancelReason("用户取消");
        orders2.setCancelTime(LocalDateTime.now());
        orderMapper.update(orders2);
    }

    //再来一单
    @Override
    public void repetition(Long id) {
        //根据id查询订单详情
        List<OrderDetail> orderDetails = orderMapper.getOrderDetailById(id);
        //将订单详情对象转化为购物车对象
        List<ShoppingCart> shoppingCarts = orderDetails.stream().map(s -> {
            ShoppingCart shoppingCart = new ShoppingCart();
            BeanUtils.copyProperties(s, shoppingCart, "id");
            shoppingCart.setUserId(JwtTokenUserInterceptor.threadLocal.get());
            shoppingCart.setCreateTime(LocalDateTime.now());
            return shoppingCart;
        }).collect(Collectors.toList());
        //插入购物车数据
        shoppingCartMapper.addAll(shoppingCarts);
    }

    //订单搜索
    @Override
    public PageResult pageUser(OrdersPageQueryDTO ordersPageQueryDTO) {
        //设置分页参数
        PageHelper.startPage(ordersPageQueryDTO.getPage(), ordersPageQueryDTO.getPageSize());
        Page<Orders> orders = orderMapper.getOrders(ordersPageQueryDTO);
        //封装返回类OrderVO
        List<OrderVO> orderVOS = orders.stream().map(s -> {
            OrderVO orderVO = new OrderVO();
            BeanUtils.copyProperties(s, orderVO);
            //查询订单详情
            List<OrderDetail> orderDetails = orderMapper.getOrderDetailById(s.getId());
            orderVO.setOrderDetailList(orderDetails);
            //根据订单id获取字符串信息
            String orderDishes = "";
            for (OrderDetail detail : orderDetails) {
                orderDishes += detail.getName() + "*" + detail.getNumber() + ";";
            }
            orderVO.setOrderDishes(orderDishes);
            return orderVO;
        }).collect(Collectors.toList());

        return new PageResult(orders.getTotal(), orderVOS);
    }

    //查看订单状态统计
    @Override
    public OrderStatisticsVO findStatistics() {
        OrderStatisticsVO orderStatisticsVO = new OrderStatisticsVO();
        //待接单的数量
        orderStatisticsVO.setToBeConfirmed(orderMapper.totalOrder(Orders.TO_BE_CONFIRMED));
        //待派送数量
        orderStatisticsVO.setConfirmed(orderMapper.totalOrder(Orders.CONFIRMED));
        //派送中数量
        orderStatisticsVO.setDeliveryInProgress(orderMapper.totalOrder(Orders.DELIVERY_IN_PROGRESS));

        return orderStatisticsVO;
    }

    //查询订单详情
    @Override
    public OrderVO getDetails(Long id) {
        //根据订单id查询订单
        Orders orders = orderMapper.getById(id);
        //根据订单id查询订单详情
        List<OrderDetail> orderDetails = orderMapper.getOrderDetailById(id);
        //封装返回数据
        OrderVO orderVO = new OrderVO();
        BeanUtils.copyProperties(orders, orderVO);
        orderVO.setOrderDetailList(orderDetails);
        //获取菜品字符串
        String orderDishes = "";
        for (OrderDetail orderDetail : orderDetails) {
            orderDishes += orderDetail.getName() + "*" + orderDetail.getNumber() + ";";
        }
        orderVO.setOrderDishes(orderDishes);
        return orderVO;
    }

    //接单
    @Override
    public void confirm(OrdersConfirmDTO ordersConfirmDTO) {
        //订单状态 1待付款 2待接单 3已接单 4派送中 5已完成 6已取消
        Orders orders = new Orders();
        orders.setId(ordersConfirmDTO.getId());
        orders.setStatus(Orders.CONFIRMED);
        //更新订单状态
        orderMapper.update(orders);
    }

    //拒单
    @Override
    public void rejection(OrdersRejectionDTO ordersRejectionDTO) {
        //根据订单id查询订单
        Orders orders = orderMapper.getById(ordersRejectionDTO.getId());
        //判断订单状态是否为待接单
        if (orders == null || !orders.getStatus().equals(Orders.TO_BE_CONFIRMED)) {
            throw new OrderBusinessException(MessageConstant.ORDER_STATUS_ERROR);
        }
        if (orders.getPayStatus().equals(Orders.PAID)) {
            //更新订单支付状态为退款
            Orders order = new Orders();
            order.setId(ordersRejectionDTO.getId());
            order.setStatus(7);
            order.setPayStatus(Orders.REFUND);
            order.setRejectionReason(ordersRejectionDTO.getRejectionReason());
            orderMapper.update(order);
            return;
        }
        Orders orders1 = new Orders();
        orders1.setId(ordersRejectionDTO.getId());
        orders1.setStatus(Orders.CANCELLED);
        orders1.setRejectionReason(ordersRejectionDTO.getRejectionReason());
        orders1.setCancelTime(LocalDateTime.now());
        orderMapper.update(orders1);
    }

    //商家取消订单
    @Override
    public void cancelAdmin(OrdersCancelDTO ordersCancelDTO) {
        //根据订单id查询订单
        Orders orders = orderMapper.getById(ordersCancelDTO.getId());

        //判断用户是否支付
        if (orders.getPayStatus().equals(Orders.PAID)){
            //为用户退款
            Orders order = new Orders();
            order.setId(ordersCancelDTO.getId());
            order.setStatus(6);
            order.setPayStatus(Orders.REFUND);
            order.setCancelTime(LocalDateTime.now());
            order.setCancelReason(ordersCancelDTO.getCancelReason());
            orderMapper.update(order);
            return;
        }
        //若用户未支付
        Orders orders1 = new Orders();
        orders1.setId(ordersCancelDTO.getId());
        orders1.setStatus(6);
        orders1.setCancelTime(LocalDateTime.now());
        orders1.setCancelReason(ordersCancelDTO.getCancelReason());
        orderMapper.update(orders1);
    }

    //派送订单
    @Override
    public void delivery(Long id) {
        // 根据id查询订单
        Orders ordersDB = orderMapper.getById(id);

        // 校验订单是否存在，并且状态为3
        if (ordersDB == null || !ordersDB.getStatus().equals(Orders.CONFIRMED)) {
            throw new OrderBusinessException(MessageConstant.ORDER_STATUS_ERROR);
        }

        Orders orders = new Orders();
        orders.setId(ordersDB.getId());
        // 更新订单状态,状态转为派送中
        orders.setStatus(Orders.DELIVERY_IN_PROGRESS);

        orderMapper.update(orders);
    }

    //完成订单
    @Override
    public void complete(Long id) {
        // 根据id查询订单
        Orders ordersDB = orderMapper.getById(id);

        // 校验订单是否存在，并且状态为4
        if (ordersDB == null || !ordersDB.getStatus().equals(Orders.DELIVERY_IN_PROGRESS)) {
            throw new OrderBusinessException(MessageConstant.ORDER_STATUS_ERROR);
        }

        Orders orders = new Orders();
        orders.setId(ordersDB.getId());
        // 更新订单状态,状态转为完成
        orders.setStatus(Orders.COMPLETED);
        orders.setDeliveryTime(LocalDateTime.now());

        orderMapper.update(orders);
    }

    //模拟订单支付
    @Override
    public void payment(OrdersPaymentDTO ordersPaymentDTO) {
        //根据订单号修改订单状态和支付状态
        Orders orders = new Orders();
        orders.setNumber(ordersPaymentDTO.getOrderNumber());
        orders.setStatus(Orders.TO_BE_CONFIRMED);
        orders.setPayStatus(Orders.PAID);
        orders.setCheckoutTime(LocalDateTime.now());
        orderMapper.update(orders);

        //支付成功后，服务端利用websocket发送客户端信息
        orders.setId(orderMapper.getOrdersByNumber(ordersPaymentDTO.getOrderNumber()));
        Map map = new HashMap<>();
        map.put("type",1);
        map.put("orderId",orders.getId());
        map.put("content","订单号："+ordersPaymentDTO.getOrderNumber());
        String json= JSON.toJSONString(map);
        webSocketServer.sendToAllClient(json);
    }

    //客户催单
    @Override
    public void reminder(Long id) {
        Orders orders = orderMapper.getById(id);
        //判断订单是否存在
        if(orders==null){
            throw new OrderBusinessException(MessageConstant.ORDER_NOT_FOUND);
        }
        Map map=new HashMap();
        map.put("type",2);
        map.put("orderId",id);
        map.put("content","订单号："+orders.getNumber());
        String json=JSON.toJSONString(map);
        webSocketServer.sendToAllClient(json);
    }
}
