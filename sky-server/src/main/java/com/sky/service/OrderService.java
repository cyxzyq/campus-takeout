package com.sky.service;

import com.sky.dto.*;
import com.sky.result.PageResult;
import com.sky.vo.OrderOverViewVO;
import com.sky.vo.OrderStatisticsVO;
import com.sky.vo.OrderSubmitVO;
import com.sky.vo.OrderVO;

public interface OrderService {
    OrderSubmitVO submit(OrdersSubmitDTO ordersSubmitDTO);

    OrderVO orderDetail(Long id);

    PageResult page(Integer page, Integer pageSize, Integer status);

    void cancel(Long id);

    void repetition(Long id);

    PageResult pageUser(OrdersPageQueryDTO ordersPageQueryDTO);

    OrderStatisticsVO findStatistics();

    OrderVO getDetails(Long id);

    void confirm(OrdersConfirmDTO ordersConfirmDTO);

    void rejection(OrdersRejectionDTO ordersRejectionDTO);

    void cancelAdmin(OrdersCancelDTO ordersCancelDTO);

    void delivery(Long id);

    void complete(Long id);

    void payment(OrdersPaymentDTO ordersPaymentDTO);

    void reminder(Long id);
}
