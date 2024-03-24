package GroepswerkJava.DigitalPlanner.service;

import GroepswerkJava.DigitalPlanner.model.ContractType;
import GroepswerkJava.DigitalPlanner.model.DaysOfWeek;
import GroepswerkJava.DigitalPlanner.repository.DaysOfWeekRepository;
import GroepswerkJava.DigitalPlanner.repository.DepartmentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Component
@Service
public class DaysOfWeekServiceImpl implements IService<DaysOfWeek> {

    @Autowired
    private DaysOfWeekRepository daysOfWeekRepository;

    @Override
    public List<DaysOfWeek> getAll() {
        return this.daysOfWeekRepository.findAll();
    }

    @Override
    public void save(DaysOfWeek daysOfWeek) {
        this.daysOfWeekRepository.save(daysOfWeek);
    }

    @Override
    public DaysOfWeek getById(long id) {
        Optional<DaysOfWeek> optional = daysOfWeekRepository.findById(id);
        DaysOfWeek daysOfWeek = null;
        if(optional.isPresent())
        {
            daysOfWeek = optional.get();
        }
        else
        {
            throw new RuntimeException("Not found");
        }
        return daysOfWeek;
    }
}
