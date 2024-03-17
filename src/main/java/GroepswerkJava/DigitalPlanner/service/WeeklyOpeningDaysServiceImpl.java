package GroepswerkJava.DigitalPlanner.service;

import GroepswerkJava.DigitalPlanner.model.WeeklyOpeningDay;
import GroepswerkJava.DigitalPlanner.repository.WeeklyOpeningDaysRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.util.List;

@Component
@Service
public class WeeklyOpeningDaysServiceImpl implements IService<WeeklyOpeningDay> {

    private final WeeklyOpeningDaysRepository weeklyOpeningDaysRepository;

    @Autowired
    public WeeklyOpeningDaysServiceImpl(WeeklyOpeningDaysRepository weeklyOpeningDaysRepository){
        this.weeklyOpeningDaysRepository = weeklyOpeningDaysRepository;
    }
    @Override
    public List<WeeklyOpeningDay> getAll() {
        return weeklyOpeningDaysRepository.findAll();
    }

    @Override
    public void save(WeeklyOpeningDay weeklyOpeningDay) {

    }

    public void saveAll(List<WeeklyOpeningDay> weeklyOpeningDayList){
        weeklyOpeningDaysRepository.saveAll(weeklyOpeningDayList);
    }

    //deletes all records in the table and resets id's back to start at 1
    public void truncateAll(){
        weeklyOpeningDaysRepository.truncateTable();

    }

    @Override
    public WeeklyOpeningDay getById(long id) {
        return null;
    }
}
