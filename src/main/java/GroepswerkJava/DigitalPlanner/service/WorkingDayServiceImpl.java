package GroepswerkJava.DigitalPlanner.service;

import GroepswerkJava.DigitalPlanner.model.WeeklyOpeningDay;
import GroepswerkJava.DigitalPlanner.model.WorkingDay;
import GroepswerkJava.DigitalPlanner.repository.WorkingDayRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;

@Component
@Service
public class WorkingDayServiceImpl implements IService<WorkingDay> {

    private final WorkingDayRepository workingDayRepository;

    @Autowired
    public WorkingDayServiceImpl(WorkingDayRepository workingDayRepository) {
        this.workingDayRepository = workingDayRepository;
    }

    @Override
    public List<WorkingDay> getAll() {
        return null;
    }

    @Override
    public void save(WorkingDay workingDay) {

    }

    @Override
    public WorkingDay getById(long id) {
        return null;
    }

    // searh for workingday by date and updates it
    @Transactional
    public void updateWorkingDay(WorkingDay exceptionDay) {
        WorkingDay workingDay = workingDayRepository.findByDate(exceptionDay.getDate());
        if (workingDay != null) {
            workingDay.setOpen(exceptionDay.isOpen());
            workingDay.setStartTime(exceptionDay.getStartTime());
            workingDay.setEndTime(exceptionDay.getEndTime());
            workingDay.setMinimalOccupation(exceptionDay.getMinimalOccupation());
            workingDayRepository.save(workingDay);
        } else {
            System.out.println("error: no working date found by this date");
        }
    }

    //use WeeklyOpeningDay to fill workingday until end of year
    public void fillRestOfYear(List<WeeklyOpeningDay> weeklyOpeningDays) {
        LocalDate startDate = LocalDate.now();
        LocalDate nextYearDate = LocalDate.of(startDate.getYear() + 1, 1, 1);
        long daysBetween = startDate.until(nextYearDate, ChronoUnit.DAYS);

        for (int i = 0; i < daysBetween; i++) {
            for (WeeklyOpeningDay weeklyOpeningDay : weeklyOpeningDays) {
                if (startDate.getDayOfWeek().toString().equalsIgnoreCase(weeklyOpeningDay.getDay())) {
                    WorkingDay workingDay = workingDayRepository.findByDate(startDate);
                    if (workingDay != null) {
                        workingDay.setOpen(weeklyOpeningDay.isOpen());
                        workingDay.setStartTime(weeklyOpeningDay.getStartTime());
                        workingDay.setEndTime(weeklyOpeningDay.getEndTime());
                        workingDay.setMinimalOccupation(weeklyOpeningDay.getMinOccupation());
                        workingDayRepository.save(workingDay);
                        break;
                    }
                }
            }
            startDate = startDate.plusDays(1);
        }
    }



    public void deleteAll() {
        workingDayRepository.deleteAll();
    }
}
