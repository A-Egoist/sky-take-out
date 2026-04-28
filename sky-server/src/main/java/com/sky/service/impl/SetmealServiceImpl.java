package com.sky.service.impl;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.sky.constant.MessageConstant;
import com.sky.constant.StatusConstant;
import com.sky.dto.SetmealDTO;
import com.sky.dto.SetmealPageQueryDTO;
import com.sky.entity.Dish;
import com.sky.entity.Setmeal;
import com.sky.entity.SetmealDish;
import com.sky.exception.DeletionNotAllowedException;
import com.sky.exception.SetmealEnableFailedException;
import com.sky.mapper.DishMapper;
import com.sky.mapper.SetmealDishMapper;
import com.sky.mapper.SetmealMapper;
import com.sky.result.PageResult;
import com.sky.service.SetmealService;
import com.sky.vo.DishItemVO;
import com.sky.vo.SetmealVO;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class SetmealServiceImpl implements SetmealService {

    @Autowired
    private SetmealMapper setmealMapper;
    @Autowired
    private SetmealDishMapper setmealDishMapper;
    @Autowired
    private DishMapper dishMapper;

    /**
     * 新增套餐
     */
    @Transactional
    public void addSetmeal(SetmealDTO setmealDTO) {
        // 向 setmeal 表中增加套餐
        Setmeal setmeal = new Setmeal();
        BeanUtils.copyProperties(setmealDTO, setmeal);
        setmealMapper.insert(setmeal);  // 需要获取插入后的 id

        // 获取 insert 生成的主键值
        Long setmealId = setmeal.getId();

        // 增加 setmeal_dish 表中增加套餐中的菜品信息
        List<SetmealDish> setmealDishes = setmealDTO.getSetmealDishes();
        if (setmealDishes != null && !setmealDishes.isEmpty()) {
            setmealDishes.forEach(setmealDish -> {
                setmealDish.setSetmealId(setmealId);
            });
            // 批量插入
            setmealDishMapper.insertBatch(setmealDishes);
        }
    }

    /**
     * 套餐分页查询
     */
    public PageResult pageQuery(SetmealPageQueryDTO setmealPageQueryDTO) {
        PageHelper.startPage(setmealPageQueryDTO.getPage(), setmealPageQueryDTO.getPageSize());
        Page<SetmealVO> page = setmealMapper.pageQuery(setmealPageQueryDTO);
        return new PageResult(page.getTotal(), page.getResult());
    }

    /**
     * 批量删除套餐
     */
    @Transactional
    public void deleteBatch(List<Long> ids) {
        // 先检查所有需要删除的套餐中，是否存在启售的套餐，如果存在，则都不能删除
        for (Long id : ids) {
            Setmeal setmeal = setmealMapper.getById(id);
            if (setmeal.getStatus() == StatusConstant.ENABLE) {
                throw new DeletionNotAllowedException(MessageConstant.SETMEAL_ON_SALE);
            }
        }

        // 批量删除 setmeal 表中的套餐数据
        setmealMapper.deleteByIds(ids);

        // 批量删除 setmeal_dish 表中的套餐菜品关系数据
        setmealDishMapper.deleteByIds(ids);
    }

    /**
     * 根据 id 查询套餐和菜品
     */
    public SetmealVO queryByIdWithDish(Long id) {
        // 根据套餐 id 查询套餐数据
        Setmeal setmeal = setmealMapper.getById(id);

        // 根据套餐 id 查询套餐菜品关系数据
        List<SetmealDish> setmealDishes = setmealDishMapper.getSetmealDishesById(id);

        // 将数据封装到 VO
        SetmealVO setmealVO = new SetmealVO();
        BeanUtils.copyProperties(setmeal, setmealVO);
        setmealVO.setSetmealDishes(setmealDishes);

        return setmealVO;
    }

    /**
     * 修改套餐
     */
    @Transactional
    public void update(SetmealDTO setmealDTO) {
        // 修改套餐基本数据
        Setmeal setmeal = new Setmeal();
        BeanUtils.copyProperties(setmealDTO, setmeal);
        setmealMapper.update(setmeal);

        // 获取套餐 id
        Long setmealId = setmealDTO.getId();

        // 修改套餐和菜品的关系数据，先删除旧的套餐和菜品的关系数据，然后新增套餐和菜品的关系数据
        setmealDishMapper.deleteById(setmealId);
        List<SetmealDish> setmealDishes = setmealDTO.getSetmealDishes();
        setmealDishes.forEach(setmealDish -> {
            setmealDish.setSetmealId(setmealId);
        });
        // 向 setmeal_dish 表中插入 n 条套餐和菜品的关系数据
        setmealDishMapper.insertBatch(setmealDishes);
    }

    /**
     * 套餐启售或停售
     */
    public void EnableOrDisable(Long id, Integer status) {
        // 启售套餐
        if (status == StatusConstant.ENABLE) {
            // 先判断套餐中的菜品是否存在停售的菜品
            List<Dish> dishes = dishMapper.getDishBySetmealId(id);
            dishes.forEach(dish -> {
                // 如果存在停售的菜品，抛出启售失败异常
                if (dish.getStatus() == StatusConstant.DISABLE) {
                    throw new SetmealEnableFailedException(MessageConstant.SETMEAL_ENABLE_FAILED);
                }
            });
        }

        // 停售套餐、启售套餐（套餐中没有停售菜品）
        Setmeal setmeal = Setmeal.builder().id(id).status(status).build();
        setmealMapper.update(setmeal);
    }

    /**
     * 条件查询
     */
    public List<Setmeal> list(Setmeal setmeal) {
        List<Setmeal> list = setmealMapper.list(setmeal);
        return list;
    }

    /**
     * 根据套餐 id 查询包含的菜品
     */
    public List<DishItemVO> getDishItemById(Long id) {
        List<DishItemVO> list = setmealMapper.getDishItemBySetmealId(id);
        return list;
    }
}
