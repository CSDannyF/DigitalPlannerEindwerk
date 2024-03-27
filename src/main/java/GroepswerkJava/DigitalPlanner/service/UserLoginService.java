package GroepswerkJava.DigitalPlanner.service;

import GroepswerkJava.DigitalPlanner.dto.UserDto;
import GroepswerkJava.DigitalPlanner.model.UserLogin;

import java.util.List;

public interface UserLoginService {
    void saveUser(UserDto userDto);

    UserLogin findByEmail(String email);

    List<UserDto> findAllUsers();
}
