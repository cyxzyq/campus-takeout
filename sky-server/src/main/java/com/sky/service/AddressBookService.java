package com.sky.service;

import com.sky.entity.AddressBook;

import java.util.List;

public interface AddressBookService {
    void add(AddressBook addressBook);

    List<AddressBook> list();

    List<AddressBook> addressBookDefault();

    void updateaddressBookDefault(Integer id);

    AddressBook getaddressBookDefaultByid(Integer id);

    void update(AddressBook addressBook);

    void delect(Integer id);
}
