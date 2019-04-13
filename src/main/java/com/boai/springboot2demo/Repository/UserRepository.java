package com.boai.springboot2demo.Repository;

import com.boai.springboot2demo.Model.User;

import java.util.List;
import java.util.Map;

public interface UserRepository {

    User saveOrUpdateUser(User user);

    void deleteUser(long id);

    User getUserById(long id);

    List<User> listUser();

    Map<String,Object> getUserMap(long userId);

    Map<String,Object> getUserMapByName(String name);
}
