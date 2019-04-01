package com.boai.springboot2demo.Mapper;

import com.boai.springboot2demo.Model.User;

public interface UserMapper {
    void saveOrUpateUser(User user);

    User getUserById(Long id);
}
