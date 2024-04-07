package GroepswerkJava.DigitalPlanner.service;

import GroepswerkJava.DigitalPlanner.dto.UserLoginDto;
import GroepswerkJava.DigitalPlanner.model.UserLogin;

import java.util.List;
import java.util.Optional;

public interface UserLoginService {
    void saveUser(UserLoginDto userLoginDto);

    UserLogin findByEmail(String email);

    UserLogin findById(Long id);

    List<UserLoginDto> findAllUsers();
}
