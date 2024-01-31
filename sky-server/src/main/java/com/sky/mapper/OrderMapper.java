package com.sky.mapper;

import com.github.pagehelper.Page;
import com.sky.dto.OrdersPageQueryDTO;
import com.sky.entity.OrderDetail;
import com.sky.entity.Orders;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.time.LocalDateTime;
import java.util.List;

@Mapper
public interface OrderMapper {

    //向订单表插入数据
    void add(Orders orders);

    //向订单明细表插入数据
    void addDetail(List<OrderDetail> orderDetailList);

    //根据订单id查询订单
    @Select("select * from orders where id=#{id}")
    Orders getById(Long id);

    //根据订单id查询订单详情表
    @Select("select * from order_detail where order_id=#{id}")
    List<OrderDetail> getOrderDetailById(Long id);

    //查询历史订单
    Page<Orders> getByUserId(@Param("aLong") Long aLong,@Param("status") Integer status);

    //更新订单
    void update(Orders orders1);

    //订单搜索
    Page<Orders> getOrders(OrdersPageQueryDTO ordersPageQueryDTO);

    //各个订单状态统计
    @Select("select count(0) from orders where status=#{status}")
    int totalOrder(Integer status);

    //对超时15分钟未付款的订单自动取消
    @Select("select * from orders where status=#{status} and order_time<#{time}")
    List<Orders> payOvertime(@Param("status") Integer status,@Param("time") LocalDateTime time);

    @Select("select id from orders where number=#{number}")
    Long getOrdersByNumber(String number);
}
