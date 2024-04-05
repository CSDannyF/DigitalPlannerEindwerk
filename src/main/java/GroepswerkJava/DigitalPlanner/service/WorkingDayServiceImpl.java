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
import java.util.ArrayList;
import java.util.List;

@Component
@Service
public class WorkingDayServiceImpl implements WorkingDayService {

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
        throw new UnsupportedOperationException("methode nog niet geimplementeerd");
    }

    @Override
    public WorkingDay getById(long id) {
        throw new UnsupportedOperationException("methode nog niet geimplementeerd");
    }

    /**
     * search for working day by date and updates it
     *
     * @param exceptionDay takes a WorkingDay as parameter to update it
     */
    @Override
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
            // TODO gebruik logger
            System.out.println("error: no working day found by this date");
        }
    }


    /**
     * Fill workingday until end of year
     *
     * @param weeklyOpeningDays weekly schedule that is used to fill workingDays
     */
    @Override
    public void fillRestOfYear(List<WeeklyOpeningDay> weeklyOpeningDays) {
        LocalDate startDate = LocalDate.now();
        LocalDate nextYearDate = LocalDate.of(startDate.getYear() + 1, 1, 1);
        long daysBetween = startDate.until(nextYearDate, ChronoUnit.DAYS);

        List<WorkingDay> workingDays = new ArrayList<>();

        for (int i = 0; i < daysBetween; i++) {
            for (WeeklyOpeningDay weeklyOpeningDay : weeklyOpeningDays) {
                if (startDate.getDayOfWeek().toString().equalsIgnoreCase(weeklyOpeningDay.getDay())) {
                    WorkingDay workingDay = workingDayRepository.findByDate(startDate);
                    if (workingDay != null) {
                        workingDay.setOpen(weeklyOpeningDay.isOpen());
                        workingDay.setStartTime(weeklyOpeningDay.getStartTime());
                        workingDay.setEndTime(weeklyOpeningDay.getEndTime());
                        workingDay.setMinimalOccupation(weeklyOpeningDay.getMinOccupation());
                        workingDays.add(workingDay);
                        break;
                    }
                }
            }
            startDate = startDate.plusDays(1);
        }
        workingDayRepository.saveAll(workingDays);
    }

    @Override
    public void saveAll(List<WorkingDay> workingDays){
        workingDayRepository.saveAll(workingDays);
    }



    public void deleteAll() {
        workingDayRepository.deleteAll();
    }
}
