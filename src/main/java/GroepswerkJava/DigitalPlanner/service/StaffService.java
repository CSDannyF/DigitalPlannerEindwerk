package GroepswerkJava.DigitalPlanner.service;

import GroepswerkJava.DigitalPlanner.dto.StaffDto;
import GroepswerkJava.DigitalPlanner.dto.UserLoginDto;
import GroepswerkJava.DigitalPlanner.model.Staff;

import java.util.List;

public interface StaffService {
    void saveStaff(StaffDto staffDto);
    StaffDto getById(long id);
    List<StaffDto> getAll();
    Staff mapToStaffModel(StaffDto staffDto);
    void updateStaff(StaffDto staffDto);
}
