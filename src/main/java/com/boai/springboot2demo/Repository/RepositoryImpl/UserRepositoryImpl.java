package com.boai.springboot2demo.Repository.RepositoryImpl;

import com.boai.springboot2demo.Mapper.UserMapper;
import com.boai.springboot2demo.Model.User;
import com.boai.springboot2demo.Repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.atomic.AtomicLong;

@Repository
public class UserRepositoryImpl implements UserRepository {

    private final UserMapper userMapper;

    @Autowired
    public UserRepositoryImpl(UserMapper userMapper) {
        this.userMapper = userMapper;
    }

    private static AtomicLong counter = new AtomicLong();

    private final ConcurrentMap<Long, User> userMap = new ConcurrentHashMap<>();

    @Override
    public User saveOrUpdateUser(User user) {
        Long id = user.getId();
        if (id == null) {
            id = counter.incrementAndGet();
            user.setId(id);
        }
        this.userMap.put(id, user);
        return user;
    }

    @Override
    public void deleteUser(long id) {
        this.userMap.remove(id);
    }

    @Override
    public User getUserById(long id) {
        return userMapper.getUserById(id);
    }

    @Override
    public List<User> listUser() {
        return userMapper.listUser();
    }

    @Override
    public Map<String, Object> getUserMap(long userId) {
        return userMapper.getUserMap(userId);
    }

    @Override
    public Map<String, Object> getUserMapByName(String name) {
        return userMapper.getUserMapByName(name);
    }
}
