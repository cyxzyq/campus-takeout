package com.sky.controller.user;

import com.sky.entity.AddressBook;
import com.sky.result.Result;
import com.sky.service.AddressBookService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

//地址
@RestController
@RequestMapping("/user/addressBook")
@Slf4j
public class AddressBookController {

    @Autowired
    private AddressBookService addressBookService;

    //新增地址
    @PostMapping
    public Result add(@RequestBody AddressBook addressBook){
        log.info("新增收货地址：{}",addressBook);
        addressBookService.add(addressBook);
        return Result.success();
    }

    //查看收货地址
    @GetMapping("/list")
    public Result<List<AddressBook>> list(){
        List<AddressBook> addressBookList=addressBookService.list();
        return Result.success(addressBookList);
    }

    //设置默认地址
    @PutMapping("/default")
    public Result updateaddressBookDefault(@RequestBody AddressBook addressBook){
        log.info("当前地址的id：{}",addressBook.getId());
        addressBookService.updateaddressBookDefault(addressBook.getId());
        return Result.success();
    }

    //查看默认地址
    @GetMapping("/default")
    public Result<List<AddressBook>> addressBookDefault(){
        List<AddressBook> addressBook=addressBookService.addressBookDefault();
        return Result.success(addressBook);
    }

    //根据id查询地址
    @GetMapping("/{id}")
    public Result<AddressBook> getaddressBookDefaultByid(@PathVariable Integer id){
        log.info("查询地址id：{}",id);
        AddressBook addressBook=addressBookService.getaddressBookDefaultByid(id);
        return Result.success(addressBook);
    }

    //根据id修改地址
    @PutMapping
    public Result update(@RequestBody AddressBook addressBook){
         log.info("修改的地址信息:{}",addressBook);
         addressBookService.update(addressBook);
        return Result.success();
    }

    //根据id删除地址
    @DeleteMapping
    public Result delect(Integer id){
        log.info("删除地址id：{}",id);
        addressBookService.delect(id);
        return Result.success();
    }
}
