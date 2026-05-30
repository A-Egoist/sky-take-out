package com.sky.mapper;

import com.sky.entity.AddressBook;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface AddressBookMapper {

    /**
     * 条件查询
     */
    List<AddressBook> list(AddressBook addressBook);

    /**
     * 新增地址
     */
    @Insert("insert  into address_book (user_id, consignee, sex, phone, province_code, province_name, city_code, city_name, district_code, district_name, detail, label, is_default) " +
            "values (#{userId}, #{consignee}, #{sex}, #{phone}, #{provinceCode}, #{provinceName}, #{cityCode}, #{cityName}, #{districtCode}, #{districtName}, #{detail}, #{label}, #{isDefault})")
    void insert(AddressBook addressBook);

    /**
     * 根据 id 查询地址
     */
    @Select("select * from address_book where id = #{id}")
    AddressBook getById(Long id);

    /**
     * 根据 id 修改地址
     */
    void update(AddressBook addressBook);

    /**
     * 根据 id 删除地址
     */
    @Delete("delete from address_book where id = #{id}")
    void deleteById(Long id);

    /**
     * 根据 userId 修改是否为默认地址
     */
    @Update("update address_book set is_default = #{isDefault} where user_id = #{userId}")
    void updateIsDefaultByUserId(AddressBook addressBook);
}
