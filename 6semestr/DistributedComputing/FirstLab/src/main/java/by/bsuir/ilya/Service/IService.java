package by.bsuir.ilya.Service;

import by.bsuir.ilya.Entity.User;
import by.bsuir.ilya.dto.UserMapper;
import by.bsuir.ilya.dto.UserRequestTo;
import by.bsuir.ilya.dto.UserResponseTo;
import by.bsuir.ilya.storage.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

public interface IService<T,K> {


    public List<T> getAll();

    public T update(K requestTo);

    public T getById(long id);

    public T deleteById(long id);

    public T add(K requestTo);

}

