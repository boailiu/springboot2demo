package com.boai.springboot2demo.Service;

import com.boai.springboot2demo.Model.User;

import java.util.List;
import java.util.Map;

public interface UserService {

    List<User> listUser();

    Map<String ,Object> getUserMap(long userId);
}
