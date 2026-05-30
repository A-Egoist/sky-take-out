package com.sky.service.impl;

import com.sky.context.BaseContext;
import com.sky.entity.AddressBook;
import com.sky.mapper.AddressBookMapper;
import com.sky.service.AddressBookService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
public class AddressBookServiceImpl implements AddressBookService {

    @Autowired
    private AddressBookMapper addressBookMapper;

    /**
     * 根据 userId, phone, isDefault 查询
     */
    public List<AddressBook> list(AddressBook addressBook) {
        return addressBookMapper.list(addressBook);
    }

    /**
     * 新增地址
     */
    public void save(AddressBook addressBook) {
        addressBook.setUserId(BaseContext.getCurrentId());  // 设置 userId
        addressBook.setIsDefault(0);  // 新增地址初始化为非默认地址
        addressBookMapper.insert(addressBook);
    }

    /**
     * 根据 id 查询
     */
    public AddressBook getById(Long id) {
        AddressBook addressBook = addressBookMapper.getById(id);
        return addressBook;
    }

    /**
     * 根据 id 修改地址
     */
    public void update(AddressBook addressBook) {
        addressBookMapper.update(addressBook);
    }

    /**
     * 根据 id 删除地址
     */
    public void deleteById(Long id) {
        addressBookMapper.deleteById(id);
    }

    @Override
    public void setDefault(AddressBook addressBook) {
        // 1. 先将当前用户的所有地址设置为非默认地址
        addressBook.setIsDefault(0);  // 将当前地址设置为非默认地址
        addressBook.setUserId(BaseContext.getCurrentId());
        addressBookMapper.updateIsDefaultByUserId(addressBook);

        // 2. 然后将当前地址设置为默认地址
        addressBook.setIsDefault(1);
        addressBookMapper.update(addressBook);
    }
}
