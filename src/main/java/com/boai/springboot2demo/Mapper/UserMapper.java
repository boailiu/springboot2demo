package com.boai.springboot2demo.Mapper;

import com.boai.springboot2demo.Model.User;

import java.util.List;
import java.util.Map;

public interface UserMapper {
    void saveOrUpateUser(User user);

    User getUserById(Long id);

    List<User> listUser();

    Map<String, Object> getUserMap(long id);

    Map<String, Object> getUserMapByName(String name);
}
