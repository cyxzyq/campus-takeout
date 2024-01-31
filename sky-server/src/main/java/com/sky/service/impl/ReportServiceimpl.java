package com.sky.service.impl;

import com.google.common.base.Joiner;
import com.sky.entity.Orders;
import com.sky.mapper.ReportMapper;
import com.sky.service.ReportService;
import com.sky.vo.TurnoverReportVO;
import com.sky.vo.UserReportVO;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class ReportServiceimpl implements ReportService {

    @Autowired
    ReportMapper reportMapper;

    //统计营业额
    @Override
    public TurnoverReportVO turnoverStatistics(LocalDate begin, LocalDate end) {
        List<LocalDate> list = new ArrayList<>();
        list.add(begin);
        while (!begin.equals(end)) {
            begin = begin.plusDays(1);
            list.add(begin);
        }
        String dateList = StringUtils.join(list, ",");

        List<BigDecimal> amountList = new ArrayList<>();
        for (LocalDate date : list) {
            LocalDateTime beginTime = date.atStartOfDay();
            LocalDateTime endTime = beginTime.plusDays(1);
            Map map = new HashMap();
            map.put("begin", beginTime);
            map.put("end", endTime);
            map.put("status", Orders.COMPLETED);
            BigDecimal amountAll = reportMapper.turnoverStatistics(map);
            amountAll = amountAll == null ? BigDecimal.valueOf(0.0) : amountAll;
            amountList.add(amountAll);
        }
        String turnoverList = StringUtils.join(amountList, ",");
        return new TurnoverReportVO(dateList, turnoverList);
    }

    //用户统计
    @Override
    public UserReportVO userStatistics(LocalDate begin, LocalDate end) {
        List<LocalDate> list = new ArrayList<>();
        //将begin~end的日期放入list集合中
        while (!begin.equals(end)) {
            list.add(begin);
            begin = begin.plusDays(1);
        }
        list.add(end);
        //将list集合转换为以逗号分隔的字符串
        String dateList = StringUtils.join(list, ",");

        List<BigDecimal> bigDecimalNewList = new ArrayList<>();
        List<BigDecimal> bigDecimalTotalList=new ArrayList<>();
        //查找在begin~end内注册的新用户
        for (LocalDate date : list) {
            LocalDateTime beginTime = date.atStartOfDay();
            LocalDateTime endTime = beginTime.plusDays(1);
            //查找新增用户数量
            Map map = new HashMap();
            map.put("begin", beginTime);
            map.put("end", endTime);
            BigDecimal newUserCount = reportMapper.userStatistics(map);
            newUserCount = newUserCount == null ? BigDecimal.valueOf(0) : newUserCount;
            bigDecimalNewList.add(newUserCount);
            //查找总用户数量
             Map map1=new HashMap();
             map1.put("end",endTime);
            BigDecimal totalUser = reportMapper.userStatistics(map1);
            totalUser = totalUser == null ? BigDecimal.valueOf(0) : totalUser;
            bigDecimalTotalList.add(totalUser);
        }

        //将bigDecimalNewList集合转换为以逗号分隔的字符串
        String newUserList = StringUtils.join(bigDecimalNewList, ",");
        //将bigDecimalTotalList集合转换为以逗号分隔的字符串
        String totalUserList = StringUtils.join(bigDecimalTotalList, ",");
        return new UserReportVO(dateList,totalUserList, newUserList);
    }
}
