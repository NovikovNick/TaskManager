package com.metalheart.converter;

import com.metalheart.converter.mapper.UserMapper;
import com.metalheart.model.User;
import com.metalheart.model.jpa.UserJpa;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component
public class UserJpaToUserConverter implements Converter<UserJpa, User> {

    @Override
    public User convert(UserJpa source) {
        return UserMapper.INSTANCE.map(source);
    }
}