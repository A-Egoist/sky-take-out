package com.sky.controller.user;

import com.sky.context.BaseContext;
import com.sky.entity.AddressBook;
import com.sky.result.Result;
import com.sky.service.AddressBookService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/user/addressBook")
@Slf4j
@Api(tags = "C端地址簿接口")
public class AddressBookController {

    @Autowired
    private AddressBookService addressBookService;

    /**
     * 查询当前登录用户的所有地址信息
     */
    @GetMapping("/list")
    public Result<List<AddressBook>> list() {
        AddressBook addressBook = new AddressBook();
        addressBook.setUserId(BaseContext.getCurrentId());
        List<AddressBook> list = addressBookService.list(addressBook);
        return Result.success(list);
    }

    /**
     * 新增地址
     */
    @PostMapping
    @ApiOperation(value = "新增地址")
    public Result save(@RequestBody AddressBook addressBook) {
        addressBookService.save(addressBook);
        return Result.success();
    }

    /**
     * 根据 id 查询地址
     */
    @GetMapping("/{id}")
    @ApiOperation(value = "根据 id 查询地址")
    public Result<AddressBook> list(@PathVariable Long id) {
        AddressBook addressBook = addressBookService.getById(id);
        return Result.success(addressBook);
    }

    /**
     * 根据 id 修改地址
     */
    @PutMapping
    @ApiOperation(value = "根据 id 修改地址")
    public Result update(@RequestBody AddressBook addressBook) {
        addressBookService.update(addressBook);
        return Result.success();
    }

    /**
     * 根据 id 删除地址
     */
    @DeleteMapping
    @ApiOperation(value = "根据 id 删除地址")
    public Result delete(Long id) {
        addressBookService.deleteById(id);
        return Result.success();
    }

    /**
     * 设置默认地址
     */
    @PutMapping("/default")
    @ApiOperation(value = "设置默认地址")
    public Result setDefault(@RequestBody AddressBook addressBook) {
        addressBookService.setDefault(addressBook);
        return Result.success();
    }

    /**
     * 查询默认地址
     */
    @GetMapping("/default")
    @ApiOperation(value = "查询默认地址")
    public Result<AddressBook> getDefault() {
        AddressBook addressBook = new AddressBook();
        addressBook.setUserId(BaseContext.getCurrentId());
        addressBook.setIsDefault(1);
        // 根据 userId 和 id 查询是否存在默认地址
        List<AddressBook> list = addressBookService.list(addressBook);

        if (list != null && list.size() == 1) {
            // 存在
            return Result.success(list.get(0));
        }

        return Result.error("没有查询到默认地址");
    }
}
