package com.sky.service.impl;

import com.sky.context.BaseContext;
import com.sky.dto.ShoppingCartDTO;
import com.sky.entity.Dish;
import com.sky.entity.Setmeal;
import com.sky.entity.ShoppingCart;
import com.sky.mapper.DishMapper;
import com.sky.mapper.SetmealMapper;
import com.sky.mapper.ShoppingCartMapper;
import com.sky.service.ShoppingCartService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@Slf4j
public class ShoppingCartServiceImpl implements ShoppingCartService {

    @Autowired
    private ShoppingCartMapper shoppingCartMapper;
    @Autowired
    private DishMapper dishMapper;
    @Autowired
    private SetmealMapper setmealMapper;

    /**
     * 添加购物车
     */
    public void addShoppingCart(ShoppingCartDTO shoppingCartDTO) {
        // 判断当前加入购物车中的商品是否已经存在
        // select * from shopping_cart where user_id = ? and setmeal_id = ?
        // select * from shopping_cart where user_id = ? and dish_id = ? and dish_flavor = ?
        ShoppingCart shoppingCart = new ShoppingCart();
        BeanUtils.copyProperties(shoppingCartDTO, shoppingCart);
        Long userId = BaseContext.getCurrentId();
        shoppingCart.setUserId(userId);

        List<ShoppingCart> list = shoppingCartMapper.list(shoppingCart);

        // 如果已经存在，数量加一
        if (list != null && !list.isEmpty()) {
            ShoppingCart cart = list.get(0);
            cart.setNumber(cart.getNumber() + 1);
            shoppingCartMapper.updateNumberById(cart);
        } else {
            // 如果不存在，插入一条购物车数据
            // 判断添加的是菜品还是套餐
            Long dishId = shoppingCartDTO.getDishId();
            if (dishId != null) {
                // 添加的是菜品
                Dish dish = dishMapper.getById(dishId);
                shoppingCart.setName(dish.getName());
                shoppingCart.setImage(dish.getImage());
                shoppingCart.setAmount((dish.getPrice()));
            } else {
                // 添加的是套餐
                Long setmealId = shoppingCartDTO.getSetmealId();
                Setmeal setmeal = setmealMapper.getById(setmealId);
                shoppingCart.setName(setmeal.getName());
                shoppingCart.setImage(setmeal.getImage());
                shoppingCart.setAmount((setmeal.getPrice()));
            }
            shoppingCart.setNumber(1);
            shoppingCart.setCreateTime(LocalDateTime.now());
            shoppingCartMapper.insert(shoppingCart);
        }
    }

    /**
     * 查看购物车
     */
    public List<ShoppingCart> showShoppingCart() {
        Long userId = BaseContext.getCurrentId();
        ShoppingCart shoppingCart = ShoppingCart.builder().userId(userId).build();
        List<ShoppingCart> list = shoppingCartMapper.list(shoppingCart);
        return list;
    }

    /**
     * 清空购物车
     */
    public void cleanShoppingCart() {
        Long userId = BaseContext.getCurrentId();
        shoppingCartMapper.deleteByUserId(userId);
    }

    /**
     * 删除购物车中一个商品
     */
    public void subShoppingCart(ShoppingCartDTO shoppingCartDTO) {
        // 构建购物车对象
        ShoppingCart shoppingCart = new ShoppingCart();
        BeanUtils.copyProperties(shoppingCartDTO, shoppingCart);
        shoppingCart.setUserId(BaseContext.getCurrentId());

        // 查询当前登录用户的购物车数据
        List<ShoppingCart> list = shoppingCartMapper.list(shoppingCart);
        if (list != null && !list.isEmpty()) {
            ShoppingCart cart = list.get(0);  // 只读
            shoppingCart.setId(cart.getId());

            Integer number = cart.getNumber();
            if (number == 1) {
                // 如果商品在购物车中的数量为 1，则直接删除该条记录
                shoppingCartMapper.deleteById(shoppingCart.getId());
            } else {
                // 如果商品在购物车中的数量大于 1，则数量减 1
                shoppingCart.setNumber(number - 1);
                shoppingCartMapper.updateNumberById(shoppingCart);
            }
        }
    }
}
