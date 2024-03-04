package GroepswerkJava.DigitalPlanner.service;

import GroepswerkJava.DigitalPlanner.model.Staff;

import java.util.List;

public interface StaffService {
    List<Staff> getAllStaff();

    void saveStaff(Staff staff);

    Staff getStaffById(Long id);
}
