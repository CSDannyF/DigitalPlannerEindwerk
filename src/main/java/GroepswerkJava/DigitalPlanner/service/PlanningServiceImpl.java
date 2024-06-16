package GroepswerkJava.DigitalPlanner.service;

import GroepswerkJava.DigitalPlanner.dto.*;
import GroepswerkJava.DigitalPlanner.exceptions.DuplicateStaffException;
import GroepswerkJava.DigitalPlanner.model.*;
import GroepswerkJava.DigitalPlanner.repository.*;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.*;

@Component
//@Service
public class PlanningServiceImpl implements PlanningService{

    @Autowired
    private PlanningRepository planningRepository;

    @Autowired
    private StaffServiceImpl staffService;

    @Autowired
    private AvailabilityServiceImpl availabilityService;

    @Autowired
    private PlanningStatusRepository planningStatusRepository;

    @Autowired
    private WorkingDayRepository workingDayRepository;

    @Autowired
    private StaffRepository staffRepository;

    @Autowired
    private WorkingDayService workingDayService;

    @Autowired
    private PlanningStatusService planningStatusService;

    @Override
    public PlanningDto save(PlanningDto planningDto)
    {
        Planning planning =  mapToPlanningModel(planningDto);
        Planning newPlanning = planningRepository.save(planning);
        return mapToPlanningDto(newPlanning);
        //TODO nog geimplementeerd worden
    }

    @Override
    public PlanningDto update(Planning planning) {
        Planning updatedPlanning = planningRepository.save(planning);
        return mapToPlanningDto(updatedPlanning);
    }


    @Override
    public PlanningDto savePlanningDetails(PlanningDto planningDto, ListPlanningDetailDto listPlanningDetailDto, String plannedBy) {
        WorkingDay workingDay = workingDayService.getById(planningDto.getWorkingDayDtoId());
        planningDto.setWorkingDayDto(workingDayService.mapToDto(workingDay));

        PlanningDto exising = getByWorkingDay(workingDayService.mapToDto(workingDay));
        if(exising.getPlanningStatusDto().getPlanningStatusName().equalsIgnoreCase("Planned OK")){
            planningDto.setPlanningStatusDto(planningStatusService.findByPlanningStatusName("Planned OK"));
        } else {
            planningDto.setPlanningStatusDto(planningStatusService.findByPlanningStatusName("Planned NOK"));
        }
        planningDto.setPlannedByStaff(plannedBy);
        planningDto.setPlannedDate(LocalDate.now());

        if (planningDto.getPlanningDetails() != null) {
            for (PlanningDetailDto detail : planningDto.getPlanningDetails()) {
                if (detail.getStaff() != null) {
                    StaffDto staff = staffService.getById(detail.getStaff().getStaffId());
                    detail.setStaff(staff);
                }
            }
        } else {
            planningDto.setPlanningDetails(new ArrayList<>());
        }

        List<PlanningDetailDto> newPlanningDetails = new ArrayList<>();
        if (listPlanningDetailDto != null) {
            for (PlanningDetailDto newDetail : listPlanningDetailDto.getNewPlanningDetails()) {
                if (newDetail.getStartTime() != null && newDetail.getEndTime() != null) {
                    long staffId = newDetail.getStaffId();
                    if (staffId != 0) {
                        PlanningDetailDto newPlanningDetail = createShift(staffId, newDetail.getStartTime(), newDetail.getEndTime());
                        newPlanningDetails.add(newPlanningDetail);
                    }
                }
            }
        }

        // Combine old and new planning details
        List<PlanningDetailDto> allPlanningDetails = new ArrayList<>(planningDto.getPlanningDetails());
        allPlanningDetails.addAll(newPlanningDetails);

        // Check for duplicate staff in combined planning details
        if (hasDuplicateStaff(allPlanningDetails)) {
            throw new DuplicateStaffException("Failed planning edit: It is not possible to plan the same staff more than once per day");
        }

        // If no duplicates, update hoursInDebt and add new planning details to the planningDto
        for (PlanningDetailDto newDetail : newPlanningDetails) {
            updateStaffHoursInDebt(newDetail);
            planningDto.getPlanningDetails().add(newDetail);
        }

        Planning newPlanning = planningRepository.save(mapToPlanningModel(planningDto));
        return mapToPlanningDto(newPlanning);
    }

    private PlanningDetailDto createShift(long staffId, LocalTime startTime, LocalTime endTime) {
        StaffDto staff = staffService.getById(staffId);
        PlanningDetailDto newPlanningDetail = new PlanningDetailDto();
        newPlanningDetail.setStartTime(startTime);
        newPlanningDetail.setEndTime(endTime);
        newPlanningDetail.setStaff(staff);
        newPlanningDetail.setAvailability(availabilityService.getByAvailabilityType("Planned"));
        return newPlanningDetail;
    }

    private void updateStaffHoursInDebt(PlanningDetailDto planningDetail) {
        StaffDto staff = planningDetail.getStaff();
        Duration duration = Duration.between(planningDetail.getStartTime(), planningDetail.getEndTime());
        staff.setHoursInDebt((int) (staff.getHoursInDebt() - duration.toHours()));
        staffService.saveOrUpdateStaff(staffService.mapToStaffModel(staff));
    }


    /*
    Om de save te laten werken om een update te doen moeten de ID's van alle modellen die al in de database staan geset worden
    want bijna alle modellen hebben velden die uniek zijn of er linken mee. anders gaat die in fout over duplicates.
    enkel bij de models die nog niet in de database staan hoeft dit niet want dit zijn nieuwe records (PlanningDetail)
     */
    @Override
    public List<PlanningDto> saveAll(List<PlanningDto> planningDtoList) {
        List<Planning> plannings = new ArrayList<>();
        for(PlanningDto planningDto: planningDtoList)
        {
            Planning planning = mapToPlanningModel(planningDto);
            plannings.add(planning);
        }
        List<Planning> newPlannings = planningRepository.saveAll(plannings);
        List<PlanningDto> newPlanningDtos = new ArrayList<>();
        for(Planning newPlanning: newPlannings)
        {
            PlanningDto newPlanningDto = new PlanningDto();
            newPlanningDto = mapToPlanningDto(newPlanning);
            newPlanningDtos.add(newPlanningDto);
        }
        return newPlanningDtos;
    }

    @Override
    public PlanningDto getByWorkingDay(WorkingDayDto workingDayDto) {
        return mapToPlanningDto(planningRepository.findByWorkingDay(mapFromWorkingDayDto(workingDayDto)));
    }

    // check if no duplicate staff is planned in same planning
    @Override
    public boolean hasDuplicateStaff(List<PlanningDetailDto> planningDetails) {
        Set<String> staffNames = new HashSet<>();
        for (PlanningDetailDto detail : planningDetails) {
            if(detail.getStaff() != null){
                if (!staffNames.add(detail.getStaff().getFirstName() + ", " + detail.getStaff().getLastName())) {
                    return true;
                }
            }
        }
        return false;
    }


    //saves a range (start and end date) of sickness or absence unavailabilities
    @Override
    public void saveUnavailabilities(LocalDate startDate, LocalDate endDate, Long unavailabilityId, long staffId) {
        List<WorkingDay> days = new ArrayList<>();

        Staff staff = staffService.getModelById(staffId);
        Optional<Availability> availabilityOpt = availabilityService.getModelById(unavailabilityId);
        Availability availability = null;
        if(availabilityOpt.isPresent()){
            availability = availabilityOpt.get();
        }

        LocalDate date = startDate;
        while (!date.isAfter(endDate)) {
            WorkingDay workingDay = workingDayService.getModelByDate(date);

            for(PlanningDetail planningDetail: workingDay.getPlanning().getPlanningDetails()){
                if(planningDetail.getStaff() != null && planningDetail.getStaff().getStaffId() == staff.getStaffId()){
                    planningDetail.setStaff(null);
                    planningDetail.setAvailability(availabilityService.mapToAvailabilityModel(availabilityService.getByAvailabilityType("OPEN SHIFT")));
//                    planningDetail.setAvailability(availability);
                    workingDay.getPlanning().setPlanningStatus(planningStatusService.findByStatusName("Planned NOK"));

                }
            }
            PlanningDetail planningDetail = new PlanningDetail();
            planningDetail.setStaff(staff);
            planningDetail.setAvailability(availability);
//            workingDay.getPlanning().setPlanningStatus(planningStatusService.findByStatusName("Planned NOK"));
            planningDetail.setPlanning(workingDay.getPlanning());
            workingDay.getPlanning().getPlanningDetails().add(planningDetail);

            days.add(workingDay);

            date = date.plusDays(1);
        }
        workingDayService.saveAll(days);
    }

    @Override
    public List<StaffDto> getAvailableStaff(List<PlanningDetailDto> planningDetails) {

        List<StaffDto> staffList = staffService.getActiveStaff();
        //removing staff that is already planned on this day, staffList is for dropDown select
        for(PlanningDetailDto planningDetailDto: planningDetails){
            if(planningDetailDto.getAvailability().getAvailabilityType().equalsIgnoreCase("Planned")
                    || planningDetailDto.getAvailability().getAvailabilityType().equalsIgnoreCase("Sick")
                    || planningDetailDto.getAvailability().getAvailabilityType().equalsIgnoreCase("Absence")){
                staffList.remove(planningDetailDto.getStaff());
            }
        }
        return staffList;
    }


    //Dto mappers

    private WorkingDay mapFromWorkingDayDto(WorkingDayDto workingDayDto){    //effe op public, stond op private
        WorkingDay workingDay = new WorkingDay();
        workingDay.setWorkingDayId(workingDayDto.getWorkingDayId());
        workingDay.setDate(workingDayDto.getDate());
        workingDay.setOpen(workingDayDto.isOpen());
        workingDay.setMinimalOccupation(workingDayDto.getMinimalOccupation());
        workingDay.setStartTime(workingDayDto.getStartTime());
        workingDay.setEndTime(workingDayDto.getEndTime());
        return workingDay;
    }

    protected PlanningDto mapToPlanningDto(Planning planning){
        PlanningDto planningDto = new PlanningDto();
        PlanningStatusServiceImpl planningStatusService = new PlanningStatusServiceImpl(planningStatusRepository);

        if(planning != null){

            planningDto.setPlanningId(planning.getPlanningId()); //id zou hier via de WorkingDay moeten worden opgehaald (getByDate())
            if(planning.getPlanningStatus() != null){

                //map to planningStatusDto
                PlanningStatusDto planningStatusDto = planningStatusService.mapToPlanningStatusDto(planning.getPlanningStatus());
                planningDto.setPlanningStatusDto(planningStatusDto);
            }
            planningDto.setPlannedByStaff(planning.getPlannedByStaff());
            planningDto.setPlannedDate(planning.getPlannedDate());
            planningDto.setWorkingDayDtoId(planning.getWorkingDay().getWorkingDayId());
            List<PlanningDetailDto> planningDetailDtoList = new ArrayList<>();
            for(PlanningDetail planningDetail: planning.getPlanningDetails()){
                PlanningDetailDto planningDetailDto = mapToPlanningDetailDto(planningDetail);
                planningDetailDtoList.add(planningDetailDto);
            }
            planningDto.setPlanningDetails(planningDetailDtoList);
        } else {
            planningDto.setPlanningStatusDto(planningStatusService.findByPlanningStatusName("Not planned"));
        }
        return planningDto;
    }

    @Override
    public PlanningDetailDto mapToPlanningDetailDto(PlanningDetail planningDetail){
        PlanningDetailDto planningDetailDto = new PlanningDetailDto();
        planningDetailDto.setPlanningDetailId(planningDetail.getPlanningDetailId());  //setten van Id zou weg mogen als bij het mappen naar model de ID geset wordt via WorkingDay
        planningDetailDto.setStartTime(planningDetail.getStartTime());
        planningDetailDto.setEndTime(planningDetail.getEndTime());
        planningDetailDto.setWorkMinutesPerDay(planningDetail.getWorkMinutesPerDay());

        StaffServiceImpl service = new StaffServiceImpl(staffRepository);

        if(planningDetail.getStaff() != null){
            StaffDto staffDto = service.mapToStaffDto(planningDetail.getStaff());
            planningDetailDto.setStaff(staffDto);
        } else {
            planningDetailDto.setStaff(null);

        }

        if(planningDetail.getAvailability() != null){
            AvailabilityServiceImpl availabilityService1 = new AvailabilityServiceImpl();
            AvailabilityDto availabilityDto = availabilityService1.mapToAvailabilityDto(planningDetail.getAvailability());
            planningDetailDto.setAvailability(availabilityDto);
        }
        return planningDetailDto;
    }

    protected Planning mapToPlanningModel(PlanningDto planningDto)
    {
        Planning planning = new Planning();

        planning.setPlannedByStaff(planningDto.getPlannedByStaff());
        planning.setPlannedDate(planningDto.getPlannedDate());
        planning.setPlanningDetails(mapToPlanningDetailModel(planningDto.getPlanningDetails()));



        WorkingDay workingDay = workingDayRepository.findByDate(planningDto.getWorkingDayDto().getDate());
        if(!workingDay.getPlanning().getPlanningDetails().isEmpty())
        {
            for (PlanningDetail repoPlanningDetail: workingDay.getPlanning().getPlanningDetails())
            {
                for (PlanningDetail mappedPlanningDetail: planning.getPlanningDetails())
                {
                    if(repoPlanningDetail.getStaff() == mappedPlanningDetail.getStaff())
                    {
                        mappedPlanningDetail.setPlanningDetailId(repoPlanningDetail.getPlanningDetailId());
                    }
                }
            }
        }

        planning.setPlanningId(planningDto.getPlanningId());  //TODO setten van Id zou via de workingDay moeten

        WorkingDayServiceImpl workingDayService = new WorkingDayServiceImpl(workingDayRepository, planningStatusRepository);
        planning.setWorkingDay(workingDayService.mapToWorkingDayModel(planningDto.getWorkingDayDto()));

        PlanningStatusServiceImpl planningStatusService = new PlanningStatusServiceImpl(planningStatusRepository);
        if(planningDto.getPlanningStatusDto() != null){
            PlanningStatus planningStatus = planningStatusService.mapToPlanningStatus(planningDto.getPlanningStatusDto());
            planning.setPlanningStatus(planningStatusRepository.findByPlanningStatusName(planningStatus.getPlanningStatusName()));
        }

        for(PlanningDetail planningDetail: planning.getPlanningDetails())
        {
            planningDetail.setPlanning(planning);
        }

        return  planning;
    }

    protected List<PlanningDetail> mapToPlanningDetailModel(List<PlanningDetailDto> planningDetailDtoList)
    {
        List<PlanningDetail> planningDetails = new ArrayList<>();

        for(PlanningDetailDto planningDetailDto: planningDetailDtoList)
        {


            PlanningDetail planningDetail = new PlanningDetail();
            planningDetail.setStartTime(planningDetailDto.getStartTime());
            planningDetail.setEndTime(planningDetailDto.getEndTime());
            planningDetail.setWorkMinutesPerDay(planningDetailDto.getWorkMinutesPerDay());

            //TODO als er 2 personen met dezelfde naam zijn wordt de planning niet aangemaakt via email address

            if(planningDetailDto.getStaff() != null){
                Staff staff = staffRepository.findByStaffLastNameAndStaffFirstName(planningDetailDto.getStaff().getLastName(), planningDetailDto.getStaff().getFirstName());
                planningDetail.setStaff(staff);
            } else {
                planningDetail.setStaff(null);
            }

            if(planningDetailDto.getAvailability() != null)
            {
                Availability availability = availabilityService.mapToAvailabilityModel(planningDetailDto.getAvailability());
                planningDetail.setAvailability(availability);
            }
            planningDetails.add(planningDetail);
        }

        return planningDetails;
    }
}
