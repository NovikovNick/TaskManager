package com.metalheart.converter;

import com.metalheart.converter.mapper.UserViewModelMapper;
import com.metalheart.model.User;
import com.metalheart.model.response.UserViewModel;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component
public class UserToUserViewModelConverter implements Converter<User, UserViewModel> {

    @Override
    public UserViewModel convert(User source) {
        return UserViewModelMapper.INSTANCE.map(source);
    }
}