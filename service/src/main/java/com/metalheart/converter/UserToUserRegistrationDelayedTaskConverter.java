package com.metalheart.converter;

import com.metalheart.converter.mapper.UserMapper;
import com.metalheart.model.User;
import com.metalheart.model.request.UserRegistrationDelayedTask;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component
public class UserToUserRegistrationDelayedTaskConverter implements Converter<User, UserRegistrationDelayedTask> {

    @Override
    public UserRegistrationDelayedTask convert(User source) {
        return UserMapper.INSTANCE.map(source);
    }
}