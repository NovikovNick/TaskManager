package com.metalheart.converter;

import com.metalheart.converter.mapper.UserViewModelMapper;
import com.metalheart.model.User;
import com.metalheart.model.request.UserRegistrationRequest;
import com.metalheart.model.response.UserViewModel;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component
public class UserRegistrationRequestToUserConverter implements Converter<UserRegistrationRequest, User> {

    @Override
    public User convert(UserRegistrationRequest source) {
        return UserViewModelMapper.INSTANCE.map(source);
    }
}