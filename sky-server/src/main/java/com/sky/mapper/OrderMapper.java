package com.sky.mapper;

import com.sky.entity.Orders;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface OrderMapper {

    /**
     * 插入订单数据，需要返回主键值
     */
    void insert(Orders orders);

    /**
     * 根据 orderNumber 和 userId 查询订单
     */
    @Select("select * from orders where number = #{orderNumber} and user_id = #{userId}")
    Orders getByNumberAndUserId(String orderNumber, Long userId);

    void update(Orders orders);
}
