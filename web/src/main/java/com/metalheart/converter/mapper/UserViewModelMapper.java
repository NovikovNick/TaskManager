package com.metalheart.converter.mapper;

import com.metalheart.model.User;
import com.metalheart.model.request.UserRegistrationRequest;
import com.metalheart.model.response.UserViewModel;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface UserViewModelMapper {

    UserViewModelMapper INSTANCE = Mappers.getMapper(UserViewModelMapper.class);

    UserViewModel map(User src);

    User map(UserRegistrationRequest src);
}
