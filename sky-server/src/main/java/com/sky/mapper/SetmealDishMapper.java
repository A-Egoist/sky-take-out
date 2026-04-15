package com.sky.mapper;

import com.sky.entity.SetmealDish;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface SetmealDishMapper {

    /**
     * 根据菜品 id 查询对应的套餐 id
     */
    // select setmeal_id from setmeal_dish where dish_id in (1, 2, 3, 4)
    List<Long> getSetmealIdsByDishIds(List<Long> dishIds);

    /**
     * 批量插入套餐菜品数据
     */
    void insertBatch(List<SetmealDish> setmealDishes);
}
