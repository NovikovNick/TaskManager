package com.metalheart.converter.mapper;

import com.metalheart.model.User;
import com.metalheart.model.jpa.UserJpa;
import com.metalheart.model.request.UserRegistrationDelayedTask;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface UserMapper {

    UserMapper INSTANCE = Mappers.getMapper(UserMapper.class);

    User map(UserJpa src);

    UserRegistrationDelayedTask map(User src);

    User map(UserRegistrationDelayedTask src);
}
