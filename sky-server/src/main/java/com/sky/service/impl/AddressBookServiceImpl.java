package com.sky.service.impl;

import com.sky.entity.AddressBook;
import com.sky.interceptor.JwtTokenUserInterceptor;
import com.sky.mapper.AddressBookMapper;
import com.sky.service.AddressBookService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

//用户地址
@Service
public class AddressBookServiceImpl implements AddressBookService {

    @Autowired
    private AddressBookMapper addressBookMapper;

    //新增收货地址
    @Override
    public void add(AddressBook addressBook) {
        //获取当前用户id
        addressBook.setUserId(JwtTokenUserInterceptor.threadLocal.get());
        //新增操作数据库
        addressBookMapper.add(addressBook);
    }

    //查看用户地址
    @Override
    public List<AddressBook> list() {
        AddressBook addressBook = new AddressBook();
        addressBook.setUserId(JwtTokenUserInterceptor.threadLocal.get());
        List<AddressBook> addressBookList=addressBookMapper.list(addressBook);
        return addressBookList;
    }

    //查看当前用户的默认地址
    @Override
    public List<AddressBook> addressBookDefault() {
        AddressBook addressBook = new AddressBook();
        addressBook.setUserId(JwtTokenUserInterceptor.threadLocal.get());
        addressBook.setIsDefault(1);
        return addressBookMapper.list(addressBook);
    }

    //设置默认地址
    @Override
    @Transactional
    public void updateaddressBookDefault(Integer id) {
        AddressBook addressBook = new AddressBook();
        addressBook.setUserId(JwtTokenUserInterceptor.threadLocal.get());
        addressBook.setIsDefault(1);
        List<AddressBook> list = addressBookMapper.list(addressBook);
        if(list!=null && list.size()>0){
            Integer id1 = list.get(0).getId();
            addressBook.setId(id1);
            addressBook.setIsDefault(0);
            addressBookMapper.updateaddressBookDefault(addressBook);
        }
        AddressBook addressBook1 = new AddressBook();
        addressBook1.setIsDefault(1);
        addressBook1.setId(id);
        addressBook1.setUserId(JwtTokenUserInterceptor.threadLocal.get());
        addressBookMapper.updateaddressBookDefault(addressBook1);
    }

    //根据id查询地址信息
    @Override
    public AddressBook getaddressBookDefaultByid(Integer id) {
        AddressBook addressBook = new AddressBook();
        addressBook.setUserId(JwtTokenUserInterceptor.threadLocal.get());
        addressBook.setId(id);
        return addressBookMapper.listAddressBook(addressBook);
    }

    //根据id修改地址
    @Override
    public void update(AddressBook addressBook) {
          addressBookMapper.updateaddressBookDefault(addressBook);
    }

    //根据id删除地址
    @Override
    public void delect(Integer id) {
        addressBookMapper.delect(id);
    }
}
