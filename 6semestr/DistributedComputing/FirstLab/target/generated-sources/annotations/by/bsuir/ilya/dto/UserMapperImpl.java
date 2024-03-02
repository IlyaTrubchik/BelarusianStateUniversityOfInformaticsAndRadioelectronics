package by.bsuir.ilya.dto;

import by.bsuir.ilya.Entity.User;
import javax.annotation.processing.Generated;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2024-02-28T20:43:09+0300",
    comments = "version: 1.5.5.Final, compiler: javac, environment: Java 17.0.10 (Amazon.com Inc.)"
)
public class UserMapperImpl implements UserMapper {

    @Override
    public UserResponseTo userToUserResponseTo(User user) {
        if ( user == null ) {
            return null;
        }

        UserResponseTo userResponseTo = new UserResponseTo();

        userResponseTo.setPassword( user.getPassword() );
        userResponseTo.setLogin( user.getLogin() );
        userResponseTo.setLastname( user.getLastname() );
        userResponseTo.setFirstname( user.getFirstname() );
        userResponseTo.setId( user.getId() );

        return userResponseTo;
    }

    @Override
    public UserRequestTo userToUserRequestTo(User user) {
        if ( user == null ) {
            return null;
        }

        UserRequestTo userRequestTo = new UserRequestTo();

        userRequestTo.setLogin( user.getLogin() );
        userRequestTo.setPassword( user.getPassword() );
        userRequestTo.setLastname( user.getLastname() );
        userRequestTo.setFirstname( user.getFirstname() );

        return userRequestTo;
    }

    @Override
    public User userResponseToToUser(UserResponseTo userResponseTo) {
        if ( userResponseTo == null ) {
            return null;
        }

        User user = new User();

        user.setId( userResponseTo.getId() );
        user.setFirstname( userResponseTo.getFirstname() );
        user.setLastname( userResponseTo.getLastname() );
        user.setLogin( userResponseTo.getLogin() );
        user.setPassword( userResponseTo.getPassword() );

        return user;
    }

    @Override
    public User userRequestToToUser(UserRequestTo userRequestTo) {
        if ( userRequestTo == null ) {
            return null;
        }

        User user = new User();

        user.setFirstname( userRequestTo.getFirstname() );
        user.setLastname( userRequestTo.getLastname() );
        user.setLogin( userRequestTo.getLogin() );
        user.setPassword( userRequestTo.getPassword() );

        return user;
    }
}
