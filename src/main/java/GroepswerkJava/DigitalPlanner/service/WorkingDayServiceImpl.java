package GroepswerkJava.DigitalPlanner.service;

import GroepswerkJava.DigitalPlanner.dto.WorkingDayDto;
import GroepswerkJava.DigitalPlanner.model.Planning;
import GroepswerkJava.DigitalPlanner.model.WeeklyOpeningDay;
import GroepswerkJava.DigitalPlanner.model.WorkingDay;
import GroepswerkJava.DigitalPlanner.repository.PlanningStatusRepository;
import GroepswerkJava.DigitalPlanner.repository.WorkingDayRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.time.temporal.WeekFields;
import java.util.ArrayList;
import java.util.List;

@Component
@Service
public class WorkingDayServiceImpl implements WorkingDayService {

    private final WorkingDayRepository workingDayRepository;

    private final PlanningStatusRepository planningStatusRepository;

    @Autowired
    public WorkingDayServiceImpl(WorkingDayRepository workingDayRepository, PlanningStatusRepository planningStatusRepository) {
        this.workingDayRepository = workingDayRepository;
        this.planningStatusRepository = planningStatusRepository;
    }

    @Override
    public List<WorkingDay> getAll() {
        return workingDayRepository.findAll();
    }

    @Override
    public List<WorkingDayDto> getByWeekNumber(int weekNumber) {
        List<WorkingDay> workingDays = workingDayRepository.findByWeekNumber(weekNumber);
        return mapToDtoList(workingDays);
    }

    @Override
    public List<WorkingDayDto> getAllDto(){
        return mapToDtoList(workingDayRepository.findAll());
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
     * search for working day by date and updates opening times
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
        List<Planning> plannings = new ArrayList<>();

        for (int i = 0; i < daysBetween; i++) {
            for (WeeklyOpeningDay weeklyOpeningDay : weeklyOpeningDays) {
                if (startDate.getDayOfWeek().toString().equalsIgnoreCase(weeklyOpeningDay.getDay())) {
                    WorkingDay workingDay = workingDayRepository.findByDate(startDate);
                    if (workingDay == null) {
                        workingDay = new WorkingDay();
                        workingDay.setDate(startDate);
                    }
                    workingDay.setOpen(weeklyOpeningDay.isOpen());
                    workingDay.setStartTime(weeklyOpeningDay.getStartTime());
                    workingDay.setEndTime(weeklyOpeningDay.getEndTime());
                    workingDay.setMinimalOccupation(weeklyOpeningDay.getMinOccupation());
                    workingDay.setWeekNumber(workingDay.getDate().get(WeekFields.ISO.weekOfWeekBasedYear()));

                    Planning planning = new Planning();
                    planning.setPlanningStatus(planningStatusRepository.findByPlanningStatusName("Not planned"));

                    planning.setWorkingDay(workingDay);
                    workingDay.setPlanning(planning);

                    workingDays.add(workingDay);
                    plannings.add(planning);
                    break;
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

    @Override
    public WorkingDayDto getByDate(LocalDate date) {
        WorkingDay workingDay = workingDayRepository.findByDate(date);
        return mapToDto(workingDay);
    }

    public void deleteAll() {
        workingDayRepository.deleteAll();
    }

    public WorkingDayDto mapToDto(WorkingDay workingDay){
        WorkingDayDto workingDayDto = new WorkingDayDto();
        if(workingDay != null){
            workingDayDto.setWorkingDayId(workingDay.getWorkingDayId());
            workingDayDto.setDate(workingDay.getDate());
            workingDayDto.setStartTime(workingDay.getStartTime());
            workingDayDto.setEndTime(workingDay.getEndTime());
            workingDayDto.setMinimalOccupation(workingDay.getMinimalOccupation());
            workingDayDto.setOpen(workingDay.isOpen());
            workingDayDto.setWeekNumber(workingDay.getWeekNumber());

            PlanningServiceImpl planningService = new PlanningServiceImpl();
            workingDayDto.setPlanningDto(planningService.mapToPlanningDto(workingDay.getPlanning()));
        }
        return workingDayDto;
    }

    protected WorkingDay mapToWorkingDayModel(WorkingDayDto workingDayDto)
    {
        WorkingDay workingDay = new WorkingDay();
        workingDay.setWorkingDayId(workingDayDto.getWorkingDayId());
        workingDay.setDate(workingDayDto.getDate());
        workingDay.setStartTime(workingDayDto.getStartTime());
        workingDay.setEndTime(workingDayDto.getEndTime());
        workingDay.setMinimalOccupation(workingDayDto.getMinimalOccupation());
        workingDay.setOpen(workingDayDto.isOpen());
        workingDay.setWeekNumber(workingDayDto.getWeekNumber());

        return workingDay;
    }

    public List <WorkingDayDto> mapToDtoList(List<WorkingDay> workingDays){
        List<WorkingDayDto> workingDayDtos = new ArrayList<>();
        for(WorkingDay workingDay: workingDays){
            WorkingDayDto workingDayDto = mapToDto(workingDay);
            workingDayDtos.add(workingDayDto);
        }
        return workingDayDtos;
    }


}
