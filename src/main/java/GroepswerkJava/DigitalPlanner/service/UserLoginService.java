package GroepswerkJava.DigitalPlanner.service;

import GroepswerkJava.DigitalPlanner.model.UserLogin;
import GroepswerkJava.DigitalPlanner.repository.UserLoginRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserLoginService implements IService<UserLogin> {

    @Autowired
    public UserLoginRepository userLoginRepository;

    @Override
    public List<UserLogin> getAll() {
        return null;
    }

    @Override
    public void save(UserLogin userLogin) {

    }

    @Override
    public UserLogin getById(long id) {
        return null;
    }

    public UserLogin getByEmail(String email)
    {
        //System.out.println(userLoginRepository.findByUsername(username));
        return userLoginRepository.findByEmail(email);
    }

}
