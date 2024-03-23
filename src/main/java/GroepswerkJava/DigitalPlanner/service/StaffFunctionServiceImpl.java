package GroepswerkJava.DigitalPlanner.service;

import GroepswerkJava.DigitalPlanner.model.StaffFunction;
import GroepswerkJava.DigitalPlanner.repository.StaffFunctionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Component
@Service
public class StaffFunctionServiceImpl implements IService<StaffFunction> {

    @Autowired
    private StaffFunctionRepository staffFunctionRepository;

    @Override
    public List<StaffFunction> getAll() {
        return this.staffFunctionRepository.findAll();
    }

    @Override
    public void save(StaffFunction staffFunction) {
        this.staffFunctionRepository.save(staffFunction);
    }

    @Override
    public StaffFunction getById(long id) {
        Optional<StaffFunction> optional = staffFunctionRepository.findById(id);
        StaffFunction staffFunction = null;
        if(optional.isPresent())
        {
            staffFunction = optional.get();
        }
        else
        {
            throw new RuntimeException("Not found");
        }
        return staffFunction;
    }
}
