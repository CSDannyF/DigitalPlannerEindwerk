package GroepswerkJava.DigitalPlanner.service;

import GroepswerkJava.DigitalPlanner.dto.UserLoginDto;

import java.util.List;

public interface UserLoginService {
    void saveUser(UserLoginDto userLoginDto);
    UserLoginDto getByEmail(String email);
    UserLoginDto getById(Long id);
    List<UserLoginDto> findAllUsers();
    void updateEmailAndPassword(UserLoginDto userLoginDto);
}
