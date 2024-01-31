package com.sky.controller.user;

import com.sky.dto.OrdersPaymentDTO;
import com.sky.dto.OrdersSubmitDTO;
import com.sky.result.PageResult;
import com.sky.result.Result;
import com.sky.service.OrderService;
import com.sky.vo.OrderSubmitVO;
import com.sky.vo.OrderVO;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.session.ResultContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

//订单
@RestController("userOrderController")
@RequestMapping("/user/order")
@Slf4j
public class OrderController {

    @Autowired
    private OrderService orderService;

    //用户下单
    @PostMapping("/submit")
    public Result<OrderSubmitVO> submit(@RequestBody OrdersSubmitDTO ordersSubmitDTO){
        log.info("用户下单：{}",ordersSubmitDTO);
        OrderSubmitVO orderSubmitVO=orderService.submit(ordersSubmitDTO);
        return Result.success(orderSubmitVO);
    }

    //查询订单详情
    @GetMapping("/orderDetail/{id}")
    public Result<OrderVO> orderDetail(@PathVariable Long id){
        log.info("查询订单id：{}",id);
         OrderVO orderVO=orderService.orderDetail(id);
        return Result.success(orderVO);
    }

    //查看历史订单
    @GetMapping("/historyOrders")
    public Result<PageResult> page(Integer page,Integer pageSize,Integer status){
        log.info("page:{},pageSize:{},status:{}",page,pageSize,status);
        PageResult pageResult=orderService.page(page,pageSize,status);
        return Result.success(pageResult);
    }

    //取消订单
    @PutMapping("/cancel/{id}")
    public Result cancel(@PathVariable Long id){
        log.info("订单id：{}",id);
        orderService.cancel(id);
        return Result.success();
    }

    //再来一单
    @PostMapping("/repetition/{id}")
    public Result repetition(@PathVariable Long id){
        log.info("再次下单的id：{}",id);
        orderService.repetition(id);
        return Result.success();
    }

    //模拟订单支付
    @PutMapping("/payment")
    public Result payment(@RequestBody OrdersPaymentDTO ordersPaymentDTO){
        log.info("模拟订单支付：{}",ordersPaymentDTO);
        orderService.payment(ordersPaymentDTO);
        return Result.success();
    }

    //客户催单
    @GetMapping("/reminder/{id}")
    public Result reminder(@PathVariable Long id){
        log.info("催单的订单id：{}",id);
        orderService.reminder(id);
        return Result.success();
    }


}
