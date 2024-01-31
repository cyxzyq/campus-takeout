package com.sky.controller.admin;

import com.sky.dto.OrdersCancelDTO;
import com.sky.dto.OrdersConfirmDTO;
import com.sky.dto.OrdersPageQueryDTO;
import com.sky.dto.OrdersRejectionDTO;
import com.sky.result.PageResult;
import com.sky.result.Result;
import com.sky.service.OrderService;
import com.sky.vo.OrderStatisticsVO;
import com.sky.vo.OrderVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

//管理端订单
@RestController("adminOrderController")
@Slf4j
@RequestMapping("/admin/order")
public class OrderController {
    @Autowired
    private OrderService orderService;

    //订单搜索
    @GetMapping("/conditionSearch")
    public Result<PageResult> conditionSearch(OrdersPageQueryDTO ordersPageQueryDTO){
        log.info("订单搜索：{}",ordersPageQueryDTO);
        PageResult pageResult=orderService.pageUser(ordersPageQueryDTO);
        return Result.success(pageResult);
    }

    //查看各个订单数量统计
    @GetMapping("/statistics")
    public Result<OrderStatisticsVO> statistics(){
        OrderStatisticsVO orderStatisticsVO=orderService.findStatistics();
        return Result.success(orderStatisticsVO);
    }

    //查询订单详情
    @GetMapping("/details/{id}")
    public Result<OrderVO> getDetails(@PathVariable Long id){
        log.info("查询订单id：{}",id);
        OrderVO orderVO=orderService.getDetails(id);
        return Result.success(orderVO);
    }

    //接单
    @PutMapping("/confirm")
    public Result confirm(@RequestBody OrdersConfirmDTO ordersConfirmDTO){
        log.info("接单：{}",ordersConfirmDTO);
        orderService.confirm(ordersConfirmDTO);
        return Result.success();
    }

    //拒单
    @PutMapping("/rejection")
    public Result rejection(@RequestBody OrdersRejectionDTO ordersRejectionDTO){
        log.info("拒单：{}",ordersRejectionDTO);
        orderService.rejection(ordersRejectionDTO);
        return Result.success();
    }

    //取消订单
    @PutMapping("/cancel")
    public Result cancel(@RequestBody OrdersCancelDTO ordersCancelDTO){
        log.info("取消订单：{}",ordersCancelDTO);
        orderService.cancelAdmin(ordersCancelDTO);
        return Result.success();
    }

    //派送订单
    @PutMapping("/delivery/{id}")
    public Result delivery(@PathVariable("id") Long id) {
        orderService.delivery(id);
        return Result.success();
    }

    //完成订单
    @PutMapping("/complete/{id}")
    public Result complete(@PathVariable("id") Long id) {
        orderService.complete(id);
        return Result.success();
    }
}
