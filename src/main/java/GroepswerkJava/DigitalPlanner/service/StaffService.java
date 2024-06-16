package GroepswerkJava.DigitalPlanner.service;

import GroepswerkJava.DigitalPlanner.dto.StaffDto;
import GroepswerkJava.DigitalPlanner.dto.UserLoginDto;
import GroepswerkJava.DigitalPlanner.dto.WorkingDayDto;
import GroepswerkJava.DigitalPlanner.model.Staff;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface StaffService {
    StaffDto saveStaff(StaffDto staffDto);

    @Transactional
    void saveOrUpdateStaff(Staff staff);

    void saveAllStaff(List<StaffDto> staffList);

    StaffDto getById(long id);

    List<StaffDto> getAll();

    StaffDto mapToStaffDto(Staff staff);

    Staff mapToStaffModel(StaffDto staffDto);

    int getTotalStaffContractHours();

    Staff getModelById(long id);
}
