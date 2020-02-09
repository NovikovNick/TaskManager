package com.metalheart.converter;

import com.metalheart.converter.mapper.UserMapper;
import com.metalheart.model.User;
import com.metalheart.model.jpa.UserJpa;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component
public class UserToUserJpaConverter implements Converter<User, UserJpa> {

    @Override
    public UserJpa convert(User source) {
        return UserMapper.INSTANCE.mapToJpa(source);
    }
}
