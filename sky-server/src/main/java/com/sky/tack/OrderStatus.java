package com.sky.tack;

import com.sky.entity.Orders;
import com.sky.mapper.OrderMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;

@Component
@Slf4j
public class OrderStatus {
    @Autowired
    private OrderMapper orderMapper;

    //对超时15分钟未付款的订单自动取消
    @Scheduled(cron = "0 * * * * ? ")
    public void payOvertime() {
        Integer status = Orders.PENDING_PAYMENT;
        LocalDateTime time=LocalDateTime.now().minusMinutes(15);
        //查找超时订单
        List<Orders> orders = orderMapper.payOvertime(status, time);
        //判断是否存在此类订单
        if (orders!=null && orders.size()>0){
            //自动取消订单
            Orders orders1 = new Orders();
            orders.forEach(o->{
                orders1.setStatus(Orders.CANCELLED);
                orders1.setCancelTime(LocalDateTime.now());
                orders1.setId(o.getId());
                orders1.setCancelReason("订单超时，自动取消");
                orderMapper.update(orders1);
                log.info("取消订单修改成功");
            });
        }
    }

    //每天凌晨1点自动将派送中的订单自动变为完成状态
    @Scheduled(cron = "0 0 1 * * ? ")
    public void outOfDelivery(){
        Integer status=Orders.DELIVERY_IN_PROGRESS;
        LocalDateTime time=LocalDateTime.now().minusMinutes(60);
        List<Orders> ordersList = orderMapper.payOvertime(status, time);
        if (ordersList!=null && ordersList.size()>0){
            Orders orders = new Orders();
            ordersList.forEach(o->{
                orders.setId(o.getId());
                orders.setStatus(Orders.COMPLETED);
                orderMapper.update(orders);
                log.info("完成订单修改成功");
            });
        }
    }
}
