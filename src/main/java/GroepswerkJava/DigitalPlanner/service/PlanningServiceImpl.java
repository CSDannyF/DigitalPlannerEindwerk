package GroepswerkJava.DigitalPlanner.service;

import GroepswerkJava.DigitalPlanner.dto.*;
import GroepswerkJava.DigitalPlanner.exceptions.DuplicateStaffException;
import GroepswerkJava.DigitalPlanner.model.*;
import GroepswerkJava.DigitalPlanner.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Component
@Service
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
    public void save(PlanningDto planningDto)
    {
        Planning planning =  mapToPlanningModel(planningDto);
        System.out.println(planning);
        planningRepository.save(planning);
        //TODO nog geimplementeerd worden
    }




    @Override
    public void savePlanningDetails(PlanningDto planningDto, ListPlanningDetailDto listPlanningDetailDto, String plannedBy) {
        WorkingDay workingDay = workingDayService.getById(planningDto.getWorkingDayDtoId());
        planningDto.setWorkingDayDto(workingDayService.mapToDto(workingDay));
        planningDto.setPlanningStatusDto(planningStatusService.findByPlanningStatusName("Planned manually"));
        planningDto.setPlannedByStaff(plannedBy);
        planningDto.setPlannedDate(LocalDate.now());

        if (planningDto.getPlanningDetails() != null) {
            for (PlanningDetailDto detail : planningDto.getPlanningDetails()) {
                StaffDto staff = staffService.getById(detail.getStaffId());
                detail.setStaff(staff);
            }
        } else {
            planningDto.setPlanningDetails(new ArrayList<>());
        }

        if (listPlanningDetailDto != null) {
            for (PlanningDetailDto newDetail : listPlanningDetailDto.getNewPlanningDetails()) {
                if (newDetail.getStartTime() != null && newDetail.getEndTime() != null) {
                    long staffId = newDetail.getStaffId();
                    if(staffId != 0){
                        StaffDto staff = staffService.getById(staffId);
                        PlanningDetailDto newPlanningDetail = new PlanningDetailDto();
                        newPlanningDetail.setStartTime(newDetail.getStartTime());
                        newPlanningDetail.setEndTime(newDetail.getEndTime());
                        newPlanningDetail.setStaff(staff);
                        planningDto.getPlanningDetails().add(newPlanningDetail);
                    }
                }
            }
        }
        // Check for duplicate staff in planning details
        if (hasDuplicateStaff(planningDto.getPlanningDetails())) {
            System.out.println("ERRRRRORRRR");
            throw new DuplicateStaffException("Failed planning edit: It is not possible to plan the same staff more than once per day");

        }

        // Save the planningDto
        planningRepository.save(mapToPlanningModel(planningDto));
    }




    /*
    Om de save te laten werken om een update te doen moeten de ID's van alle modellen die al in de database staan geset worden
    want bijna alle modellen hebben velden die uniek zijn of er linken mee. anders gaat die in fout over duplicates.
    enkel bij de models die nog niet in de database staan hoeft dit niet want dit zijn nieuwe records (PlanningDetail)
     */
    @Override
    public void saveAll(List<PlanningDto> planningDtoList) {
        List<Planning> plannings = new ArrayList<>();
        for(PlanningDto planningDto: planningDtoList)
        {
            Planning planning = mapToPlanningModel(planningDto);
            plannings.add(planning);
        }
        planningRepository.saveAll(plannings);
    }

    @Override
    public PlanningDto getByWorkingDay(WorkingDayDto workingDayDto) {
        return mapToPlanningDto(planningRepository.findByWorkingDay(mapFromWorkingDayDto(workingDayDto)));
    }

    // check if no duplicate staff is planned in same planning
    @Override
    public boolean hasDuplicateStaff(List<PlanningDetailDto> planningDetails) {
        System.out.println("duplicate?");
        Set<String> staffNames = new HashSet<>();
        for (PlanningDetailDto detail : planningDetails) {
            if (!staffNames.add(detail.getStaff().getFirstName() + ", " + detail.getStaff().getLastName())) {
                System.out.println("TRUE");
                return true;
            }
        }
        System.out.println("FALSE");
        return false;
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

    public PlanningDetailDto mapToPlanningDetailDto(PlanningDetail planningDetail){
        PlanningDetailDto planningDetailDto = new PlanningDetailDto();
        planningDetailDto.setPlanningDetailId(planningDetail.getPlanningDetailId());  //setten van Id zou weg mogen als bij het mappen naar model de ID geset wordt via WorkingDay
        planningDetailDto.setStartTime(planningDetail.getStartTime());
        planningDetailDto.setEndTime(planningDetail.getEndTime());
        planningDetailDto.setWorkMinutesPerDay(planningDetail.getWorkMinutesPerDay());

        StaffServiceImpl service = new StaffServiceImpl(staffRepository);
        StaffDto staffDto = service.mapToStaffDto(planningDetail.getStaff());
        planningDetailDto.setStaff(staffDto);

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

            Staff staff = staffRepository.findByStaffLastNameAndStaffFirstName(planningDetailDto.getStaff().getLastName(), planningDetailDto.getStaff().getFirstName());
            planningDetail.setStaff(staff);

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
