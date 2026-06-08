package com.sky.service.impl;

import com.sky.entity.Orders;
import com.sky.mapper.OrderMapper;
import com.sky.mapper.UserMapper;
import com.sky.service.ReportService;
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
}
