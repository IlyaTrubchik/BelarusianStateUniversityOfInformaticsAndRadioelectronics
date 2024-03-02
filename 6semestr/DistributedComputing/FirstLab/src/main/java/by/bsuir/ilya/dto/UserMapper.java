package by.bsuir.ilya.dto;

import by.bsuir.ilya.Entity.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper
public interface UserMapper {
    UserMapper INSTANCE = Mappers.getMapper( UserMapper.class );



    UserResponseTo userToUserResponseTo(User user);

    UserRequestTo userToUserRequestTo(User user);

    User userResponseToToUser(UserResponseTo userResponseTo);

    User userRequestToToUser(UserRequestTo userRequestTo);
}
