package com.metalheart.converter;

import com.metalheart.converter.mapper.UserMapper;
import com.metalheart.model.User;
import com.metalheart.model.request.UserRegistrationDelayedTask;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component
public class UserRegistrationDelayedTaskToUserConverter implements Converter<UserRegistrationDelayedTask, User> {

    @Override
    public User convert(UserRegistrationDelayedTask source) {
        return UserMapper.INSTANCE.map(source);
    }
}