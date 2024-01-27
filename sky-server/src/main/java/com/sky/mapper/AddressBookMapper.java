package com.sky.mapper;

import com.sky.entity.AddressBook;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface AddressBookMapper {

    //新增地址
    void add(AddressBook addressBook);

    //查看用户地址
    List<AddressBook> list(AddressBook addressBook);

    //设置默认地址
    void updateaddressBookDefault(AddressBook addressBook);

    //根据id查看地址
    @Select("select * from address_book where user_id=#{userId} and id=#{id}")
    AddressBook listAddressBook(AddressBook addressBook);

    //根据id删除地址
    @Delete("delete from address_book where id=#{id}")
    void delect(Integer id);
}
