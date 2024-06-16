package GroepswerkJava.DigitalPlanner.service;

import GroepswerkJava.DigitalPlanner.dto.ShiftDto;
import GroepswerkJava.DigitalPlanner.dto.UserLoginDto;
import GroepswerkJava.DigitalPlanner.model.Staff;

import java.io.IOException;
import java.util.List;
import java.util.Map;

public interface UserLoginService {
    UserLoginDto saveUser(UserLoginDto userLoginDto);
    UserLoginDto getByEmail(String email);
    UserLoginDto getById(Long id);
    List<UserLoginDto> findAllUsers();
    UserLoginDto updateUserLogin(UserLoginDto userLoginDto);
    UserLoginDto saveImage(UserLoginDto userLoginDto) throws IOException;
    Map<Integer, List<ShiftDto>> getUserPlanningDetails(String email);

    Staff getStaffByUserEmail(String email);
}
