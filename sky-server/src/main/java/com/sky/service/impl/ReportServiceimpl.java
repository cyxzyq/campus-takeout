package com.sky.service.impl;

import com.sky.dto.GoodsSalesDTO;
import com.sky.entity.Orders;
import com.sky.mapper.ReportMapper;
import com.sky.service.ReportService;
import com.sky.service.WorkspaceService;
import com.sky.vo.*;
import org.apache.commons.lang.StringUtils;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.FileInputStream;
import java.io.IOException;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;


@Service
public class ReportServiceimpl implements ReportService {

    @Autowired
    ReportMapper reportMapper;
    @Autowired
    WorkspaceService workspaceService;

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
        List<BigDecimal> bigDecimalTotalList = new ArrayList<>();
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
            Map map1 = new HashMap();
            map1.put("end", endTime);
            BigDecimal totalUser = reportMapper.userStatistics(map1);
            totalUser = totalUser == null ? BigDecimal.valueOf(0) : totalUser;
            bigDecimalTotalList.add(totalUser);
        }

        //将bigDecimalNewList集合转换为以逗号分隔的字符串
        String newUserList = StringUtils.join(bigDecimalNewList, ",");
        //将bigDecimalTotalList集合转换为以逗号分隔的字符串
        String totalUserList = StringUtils.join(bigDecimalTotalList, ",");
        return new UserReportVO(dateList, totalUserList, newUserList);
    }

    //订单统计
    @Override
    public OrderReportVO ordersStatistics(LocalDate begin, LocalDate end) {

        List<LocalDate> dateList = new ArrayList<>();
        //将begin~end的日期放入list集合中
        while (!begin.equals(end)) {
            dateList.add(begin);
            begin = begin.plusDays(1);
        }
        dateList.add(end);

        //每日订单数量
        List<Integer> orderCountList = new ArrayList<>();
        //每日有效订单数量
        List<Integer> validOrderCountList = new ArrayList<>();

        for (LocalDate date : dateList) {
            LocalDateTime beginTime = date.atStartOfDay();
            LocalDateTime endTime = beginTime.plusDays(1);

            //查询每天订单数量
            Map map2 = new HashMap();
            map2.put("end", endTime);
            map2.put("begin", beginTime);
            Integer orderCount = reportMapper.ordersStatistics(map2);
            orderCount = orderCount == null ? 0 : orderCount;
            orderCountList.add(orderCount);

            //查找每日有效订单数量
            map2.put("status", Orders.COMPLETED);
            Integer validOrder1 = reportMapper.ordersStatistics(map2);
            validOrder1 = validOrder1 == null ? 0 : validOrder1;
            validOrderCountList.add(validOrder1);

        }
        //计算订单总数
        Integer totalOrderCount = orderCountList.stream().reduce(Integer::sum).get();

        //计算有效订单总数
        Integer validOrderCount = validOrderCountList.stream().reduce(Integer::sum).get();

        if (totalOrderCount.equals(0)) {
            return new OrderReportVO(StringUtils.join(dateList, ","),
                    StringUtils.join(orderCountList, ","),
                    StringUtils.join(validOrderCountList, ","),
                    totalOrderCount,
                    validOrderCount,
                    0.00
            );
        }

        //订单完成率
        Double orderCompletionRate = (double) validOrderCount / (double) totalOrderCount;

        return new OrderReportVO(StringUtils.join(dateList, ","),
                StringUtils.join(orderCountList, ","),
                StringUtils.join(validOrderCountList, ","),
                totalOrderCount,
                validOrderCount,
                orderCompletionRate
        );
    }

    //查询销量排名top10
    @Override
    public SalesTop10ReportVO top(LocalDate begin, LocalDate end) {
        LocalDateTime beginTime = begin.atStartOfDay();
        LocalDateTime endTime = end.atStartOfDay().plusDays(1);

        Map map = new HashMap();
        map.put("begin", beginTime);
        map.put("end", endTime);
        map.put("status", Orders.COMPLETED);

        List<GoodsSalesDTO> list = reportMapper.top10(map);
        List<String> nameList = list.stream().map(GoodsSalesDTO::getName).collect(Collectors.toList());
        List<Integer> numberList = list.stream().map(GoodsSalesDTO::getNumber).collect(Collectors.toList());

        return new SalesTop10ReportVO(StringUtils.join(nameList, ","), StringUtils.join(numberList, ","));
    }

    //导出Excel报表
    @Override
    public void export(HttpServletResponse response) {
        //获取最近30日期数据
        LocalDate now = LocalDate.now();
        LocalDateTime end = now.minusDays(1).atTime(23, 59, 59);
        LocalDateTime begin = now.minusDays(31).atStartOfDay();
        //查询表格中所需要的数据
        BusinessDataVO data = workspaceService.getBusinessData(begin, end);

        try {
            XSSFWorkbook workbook = new XSSFWorkbook(new FileInputStream("D:\\Desktop\\苍穹外卖\\资料\\day12\\运营数据报表模板.xlsx"));
            XSSFSheet sheet1 = workbook.getSheet("Sheet1");
            XSSFRow row = sheet1.getRow(1);
            row.getCell(1).setCellValue("时间"+begin.format(DateTimeFormatter.ofPattern("yyyy-MM-dd")) + "~" + end.format(DateTimeFormatter.ofPattern("yyyy-MM-dd")));
            row = sheet1.getRow(3);
            row.getCell(2).setCellValue(data.getTurnover());
            row.getCell(4).setCellValue(data.getOrderCompletionRate());
            row.getCell(6).setCellValue(data.getNewUsers());
            row = sheet1.getRow(4);
            row.getCell(2).setCellValue(data.getValidOrderCount());
            row.getCell(4).setCellValue(data.getUnitPrice());

            for (int i = 0; i < 31; i++) {
                LocalDateTime beginTime = begin.plusDays(i);
                LocalDateTime endTime = beginTime.plusDays(1);
                data = workspaceService.getBusinessData(beginTime, endTime);
                row = sheet1.getRow(7+i);
                row.getCell(1).setCellValue(beginTime.format(DateTimeFormatter.ofPattern("yyyy-MM-dd")));
                row.getCell(2).setCellValue(data.getTurnover());
                row.getCell(3).setCellValue(data.getValidOrderCount());
                row.getCell(4).setCellValue(data.getOrderCompletionRate());
                row.getCell(5).setCellValue(data.getUnitPrice());
                row.getCell(6).setCellValue(data.getNewUsers());
            }
            ServletOutputStream outputStream = response.getOutputStream();
            workbook.write(outputStream);
            workbook.close();
            outputStream.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
