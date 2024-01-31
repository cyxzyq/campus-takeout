package com.sky.mapper;

import com.sky.dto.GoodsSalesDTO;
import org.apache.ibatis.annotations.Mapper;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

@Mapper
public interface ReportMapper {

    //统计指定日期的营业额
    BigDecimal turnoverStatistics(Map map);

    //统计用户数量
    BigDecimal userStatistics(Map map);

    //统计订单数量
    Integer ordersStatistics(Map map);

    //统计商品销量
    List<GoodsSalesDTO> top10(Map map);
}
