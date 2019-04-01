package com.boai.springboot2demo.Repository;

import com.boai.springboot2demo.Model.User;

import java.util.List;

public interface UserRepository {

    User saveOrUpdateUser(User user);

    void deleteUser(long id);

    User getUserById(long id);

    List<User> listUser();
}
