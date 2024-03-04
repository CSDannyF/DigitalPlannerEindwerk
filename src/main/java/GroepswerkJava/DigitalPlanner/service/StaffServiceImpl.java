package GroepswerkJava.DigitalPlanner.service;

import GroepswerkJava.DigitalPlanner.model.Staff;
import GroepswerkJava.DigitalPlanner.repository.StaffRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class StaffServiceImpl implements IService<Staff>{

    @Autowired
    private StaffRepository staffRepository;

    @Override
    public List<Staff> getAll() {
        return staffRepository.findAll();
    }

    @Override
    public void save(Staff staff) {
        staffRepository.save(staff);
    }

    @Override
    public Staff getById(long id) {
        Optional<Staff> optional = staffRepository.findById(id);
        Staff staff = null;
        if(optional.isPresent()){
            staff = optional.get();
        } else {
            throw new RuntimeException("Staff not found.");
        }
        return staff;
    }


}
