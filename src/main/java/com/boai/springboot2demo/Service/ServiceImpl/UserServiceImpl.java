package com.boai.springboot2demo.Service.ServiceImpl;

import com.boai.springboot2demo.Model.User;
import com.boai.springboot2demo.Repository.UserRepository;
import com.boai.springboot2demo.Service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    UserRepository repo;

    @Override
    public List<User> listUser() {
        return repo.listUser();
    }

    @Cacheable(cacheNames = "user")
    @Override
    public Map<String, Object> getUserMap(long userId) {
        return repo.getUserMap(userId);
    }
}
