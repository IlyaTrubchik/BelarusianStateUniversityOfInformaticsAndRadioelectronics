package by.bsuir.ilya.Service;

import by.bsuir.ilya.Entity.User;
import by.bsuir.ilya.dto.UserMapper;
import by.bsuir.ilya.dto.UserRequestTo;
import by.bsuir.ilya.dto.UserResponseTo;
import by.bsuir.ilya.storage.UserRepository;
import org.mapstruct.control.MappingControl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class UserService implements IService<UserResponseTo,UserRequestTo>{

    @Autowired
    private  UserRepository userRepository;

    public UserService(){

    }
    public List<UserResponseTo> getAll(){
        List<User> userList = userRepository.findAll();
        List<UserResponseTo> resultList = new ArrayList<>();
        for(int i = 0;i<userList.size();i++)
        {
            resultList.add(UserMapper.INSTANCE.userToUserResponseTo(userList.get(i)));
        }
        return resultList;
    }
    public UserResponseTo update(UserRequestTo updatingUser){
        User user = UserMapper.INSTANCE.userRequestToToUser(updatingUser);
        if(validateUser(user)) {
            boolean result = userRepository.update(user);
            UserResponseTo responseTo = result ? UserMapper.INSTANCE.userToUserResponseTo(user) : null;
            return  responseTo;
        }
        else return new UserResponseTo();
        //return responseTo;
    }
    public UserResponseTo getById(long id){
        return UserMapper.INSTANCE.userToUserResponseTo(userRepository.findById(id));
    }
    public UserResponseTo deleteById(long id)
    {
        return UserMapper.INSTANCE.userToUserResponseTo(userRepository.deleteById(id));
    }
    public UserResponseTo add(UserRequestTo userRequestTo)
    {
        User user = UserMapper.INSTANCE.userRequestToToUser(userRequestTo);
        return UserMapper.INSTANCE.userToUserResponseTo(userRepository.insert(user));
    }
    private boolean validateUser(User user)
    {
        String firstname = user.getFirstname();;
        String lastname = user.getLastname();
        String login =  user.getLogin();
        String password = user.getPassword();
        if((firstname.length()>=2 && firstname.length()<=64) &&
                (lastname.length()>=2 && lastname.length()<=64) &&
                (password.length()>=8 && firstname.length()<=128) &&
                (login.length()>=2 && login.length()<=64)) return true;
        else return false;
    }
}
