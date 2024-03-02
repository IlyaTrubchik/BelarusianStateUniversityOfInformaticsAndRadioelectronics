package by.bsuir.ilya.storage;

import by.bsuir.ilya.Entity.User;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class UserRepository implements InMemoryRepository<User>{

    Map<Long,User> userMemory = new HashMap<>();

    long lastId = 0;
    public UserRepository()
    {

    }
    @Override
    public User findById(long id) {
        User user = userMemory.get(id);
        if(user!=null) {
            user.setId(id);
        }
        return user;
    }

    @Override
    public List<User> findAll() {
        List<User> userList = new ArrayList<>();
        for (Long key:userMemory.keySet()) {
            User user = userMemory.get(key);;
            user.setId(key);
            userList.add(user);
        }
        return userList;
    }

    @Override
    public User deleteById(long id) {
        User result = userMemory.remove(id);
        if(result!=null) lastId--;
        return result;
    }

    @Override
    public boolean deleteAll() {
        userMemory = new HashMap<>();
        lastId = 0;
        return true;
    }

    @Override
    public User insert(User insertObject) {
        userMemory.put(lastId,insertObject);
        lastId++;
        return insertObject;
    }

    @Override
    public boolean updateById(Long id,User replacingUser) {
        boolean status = userMemory.replace(id,userMemory.get(id),replacingUser);
        return status;
    }

    @Override
    public boolean update(User updatingValue) {
        boolean status = userMemory.replace(updatingValue.getId(),userMemory.get(updatingValue.getId()),updatingValue);
        return status;
    }

}
