package com.sky.service.impl;

import com.sky.dto.GoodsSalesDTO;
import com.sky.entity.Orders;
import com.sky.mapper.OrderMapper;
import com.sky.mapper.UserMapper;
import com.sky.service.ReportService;
import com.sky.vo.OrderReportVO;
import com.sky.vo.SalesTop10ReportVO;
import com.sky.vo.TurnoverReportVO;
import com.sky.vo.UserReportVO;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@Slf4j
public class ReportServiceImpl implements ReportService {

    @Autowired
    private OrderMapper orderMapper;
    @Autowired
    private UserMapper userMapper;

    /**
     * 营业额统计
     */
    public TurnoverReportVO getTurnover(LocalDate begin, LocalDate end) {
        // 构造日期 list
        List<LocalDate> dateList = new ArrayList();
        dateList.add(begin);
        while (!end.equals(begin)) {
            begin = begin.plusDays(1);
            dateList.add(begin);
        }

        // 构造营业额 list
        List<Double> turnoverList = new ArrayList<>();
        for (LocalDate date : dateList) {
            LocalDateTime beginTime = LocalDateTime.of(date, LocalTime.MIN);
            LocalDateTime endTime = LocalDateTime.of(date, LocalTime.MAX);

            Map map = new HashMap();
            map.put("status", Orders.COMPLETED);
            map.put("begin", beginTime);
            map.put("end", endTime);

            Double turnover = orderMapper.sumTurnover(map);
            turnover = turnover == null ? 0.0 : turnover;

            turnoverList.add(turnover);
        }

        // 返回封装结果
        return TurnoverReportVO.builder()
                .dateList(StringUtils.join(dateList, ","))
                .turnoverList(StringUtils.join(turnoverList, ","))
                .build();
    }

    /**
     * 统计指定时间区间内的用户数据
     */
    public UserReportVO getUserStatistics(LocalDate begin, LocalDate end) {
        // 构造日期 list
        List<LocalDate> dateList = new ArrayList();
        dateList.add(begin);
        while (!end.equals(begin)) {
            begin = begin.plusDays(1);
            dateList.add(begin);
        }

        // 构造每天用户总量 list
        List<Integer> totalUserList = new ArrayList<>();
        // 构造每天新增用户 list
        List<Integer> newUserList = new ArrayList<>();

        for (LocalDate date : dateList) {
            LocalDateTime beginTime = LocalDateTime.of(date, LocalTime.MIN);
            LocalDateTime endTime = LocalDateTime.of(date, LocalTime.MAX);

            Map map = new HashMap();
            map.put("end", endTime);
            Integer totalUser = userMapper.countUser(map);
            map.put("begin", beginTime);
            Integer newUser = userMapper.countUser(map);

            totalUserList.add(totalUser);
            newUserList.add(newUser);
        }

        // 返回封装结果
        return UserReportVO.builder()
                .dateList(StringUtils.join(dateList, ","))
                .totalUserList(StringUtils.join(totalUserList, ","))
                .newUserList(StringUtils.join(newUserList, ","))
                .build();
    }

    /**
     * 统计指定时间区间内的订单数据
     */
    public OrderReportVO getOrdersStatistics(LocalDate begin, LocalDate end) {
        // 构造日期 list
        List<LocalDate> dateList = new ArrayList();
        dateList.add(begin);
        while (!end.equals(begin)) {
            begin = begin.plusDays(1);
            dateList.add(begin);
        }

        // 构造每天总订单数 list
        List<Integer> orderCountList = new ArrayList<>();
        // 构造每天有效订单数 list
        List<Integer> validOrderCountList = new ArrayList<>();

        for (LocalDate date : dateList) {
            LocalDateTime beginTime = LocalDateTime.of(date, LocalTime.MIN);
            LocalDateTime endTime = LocalDateTime.of(date, LocalTime.MAX);

            Map map = new HashMap();
            map.put("begin", beginTime);
            map.put("end", endTime);
            Integer orderCount = orderMapper.countByMap(map);
            map.put("status", Orders.COMPLETED);
            Integer validOrderCount = orderMapper.countByMap(map);

            orderCountList.add(orderCount);
            validOrderCountList.add(validOrderCount);
        }

        // 计算时间区间内的总订单数量
        Integer totalOrderCount = orderCountList.stream().reduce(Integer::sum).get();
        // 计算时间区间内的有效订单数量
        Integer validOrderCount = validOrderCountList.stream().reduce(Integer::sum).get();
        // 订单完成率
        Double orderCompletionRate = 0.0;
        if (totalOrderCount != 0) {
            orderCompletionRate = validOrderCount * 1.0 / totalOrderCount;
        }

        return OrderReportVO.builder()
                .dateList(StringUtils.join(dateList, ","))
                .orderCountList(StringUtils.join(orderCountList, ","))
                .validOrderCountList(StringUtils.join(validOrderCountList, ","))
                .totalOrderCount(totalOrderCount)
                .validOrderCount(validOrderCount)
                .orderCompletionRate(orderCompletionRate)
                .build();
    }

    /**
     * 统计指定时间区间内的销量排名 top10
     */
    public SalesTop10ReportVO getSalesTop10(LocalDate begin, LocalDate end) {
        // 转换时间格式
        LocalDateTime beginTime = LocalDateTime.of(begin, LocalTime.MIN);
        LocalDateTime endTime = LocalDateTime.of(end, LocalTime.MAX);
        
        List<GoodsSalesDTO> salesTop10 = orderMapper.getSalesTop10(beginTime, endTime);

        List<String> nameList = salesTop10.stream().map(GoodsSalesDTO::getName).collect(Collectors.toList());
        List<Integer> numberList = salesTop10.stream().map(GoodsSalesDTO::getNumber).collect(Collectors.toList());

        return SalesTop10ReportVO.builder()
                .nameList(StringUtils.join(nameList, ","))
                .numberList(StringUtils.join(numberList, ","))
                .build();
    }
}
