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
        //TODO nog geimplementeerd worden
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
