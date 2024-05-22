package GroepswerkJava.DigitalPlanner.service;

import GroepswerkJava.DigitalPlanner.dto.*;
import GroepswerkJava.DigitalPlanner.model.*;
import GroepswerkJava.DigitalPlanner.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

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

    @Override
    public void save(PlanningDto planningDto)
    {

    }

    public void savePlanningList(List<PlanningDto> planningDtos)
    {
        List<Planning> plannings = new ArrayList<>();
        for(PlanningDto planningDto: planningDtos)
        {
            Planning planning = mapToPlanningModel(planningDto);
            planning.setPlanningId(planningDto.getPlanningId());  //setten van de ID zou in de mapper moeten komen!
            plannings.add(planning);
        }
        planningRepository.saveAll(plannings);
    }

    @Override
    public PlanningDto getByWorkingDay(WorkingDayDto workingDayDto) {
        return mapToPlanningDto(planningRepository.findByWorkingDay(mapFromWorkingDayDto(workingDayDto)));
    }

    //Dto mappers

    public WorkingDay mapFromWorkingDayDto(WorkingDayDto workingDayDto){    //effe op public, stond op private
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

            planningDto.setPlanningId(planning.getPlanningId());
            if(planning.getPlanningStatus() != null){

                //map to planningStatusDto
                PlanningStatusDto planningStatusDto = planningStatusService.mapToPlanningStatusDto(planning.getPlanningStatus());
                planningDto.setPlanningStatusDto(planningStatusDto);
            }
            planningDto.setPlannedByStaff(planning.getPlannedByStaff());
            planningDto.setPlannedDate(planning.getPlannedDate());
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
        planningDetailDto.setPlanningDetailId(planningDetail.getPlanningDetailId());
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

        WorkingDayServiceImpl workingDayService = new WorkingDayServiceImpl(workingDayRepository, planningStatusRepository);
        planning.setWorkingDay(workingDayService.mapToWorkingDayModel(planningDto.getWorkingDayDto()));

        PlanningStatusServiceImpl planningStatusService = new PlanningStatusServiceImpl(planningStatusRepository);
        PlanningStatus planningStatus = planningStatusService.mapToPlanningStatus(planningDto.getPlanningStatusDto());
        planning.setPlanningStatus(planningStatusRepository.findByPlanningStatusName(planningStatus.getPlanningStatusName()));

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
            planningDetail.setWorkMinutesPerDay(planningDetail.getWorkMinutesPerDay());

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
