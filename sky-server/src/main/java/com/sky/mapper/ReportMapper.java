package com.sky.mapper;

import org.apache.ibatis.annotations.Mapper;

import java.math.BigDecimal;
import java.util.Map;

@Mapper
public interface ReportMapper {

    //统计指定日期的营业额
    BigDecimal turnoverStatistics(Map map);

    BigDecimal userStatistics(Map map);
}
