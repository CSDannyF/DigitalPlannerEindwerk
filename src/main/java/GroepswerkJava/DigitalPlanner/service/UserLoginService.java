package GroepswerkJava.DigitalPlanner.service;

import GroepswerkJava.DigitalPlanner.dto.ShiftDto;
import GroepswerkJava.DigitalPlanner.dto.UserLoginDto;
import GroepswerkJava.DigitalPlanner.model.PlanningDetail;
import GroepswerkJava.DigitalPlanner.model.Staff;
import GroepswerkJava.DigitalPlanner.model.WorkingDay;

import java.util.List;
import java.util.Map;

public interface UserLoginService {
    void saveUser(UserLoginDto userLoginDto);
    UserLoginDto getByEmail(String email);
    UserLoginDto getById(Long id);
    List<UserLoginDto> findAllUsers();
    void updateUserLogin(UserLoginDto userLoginDto);

    Map<Integer, List<ShiftDto>> getUserPlanningDetails(String email);

    Staff getStaffByUserEmail(String email);
}
