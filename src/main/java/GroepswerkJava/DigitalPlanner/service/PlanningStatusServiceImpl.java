package GroepswerkJava.DigitalPlanner.service;

import GroepswerkJava.DigitalPlanner.dto.PlanningStatusDto;
import GroepswerkJava.DigitalPlanner.model.PlanningStatus;
import GroepswerkJava.DigitalPlanner.repository.PlanningStatusRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class PlanningStatusServiceImpl implements PlanningStatusService, CommandLineRunner {

    @Autowired
    private PlanningStatusRepository planningStatusRepository;

    public PlanningStatusServiceImpl(PlanningStatusRepository planningStatusRepository)
    {
        this.planningStatusRepository = planningStatusRepository;
    }

    private static final List<String> PLANNING_STATUSES = Arrays.asList("Planned manually", "Planned OK", "Planned NOK", "Not planned", "Closed");

    /*
    @Autowired
    public PlanningStatusServiceImpl(PlanningStatusRepository planningStatusRepository)
    {
        this.planningStatusRepository = planningStatusRepository;
    }*/

    @Override
    public List<PlanningStatusDto> getAll() {
        List<PlanningStatus> planningStatusList = planningStatusRepository.findAll();
        return planningStatusList.stream()
                .map((planningStatus) -> mapToPlanningStatusDto(planningStatus))
                .collect(Collectors.toList());
    }

    @Override
    public void save(PlanningStatusDto planningStatusDto)
    {
        PlanningStatus status = new PlanningStatus();
        status.setPlanningStatusName(planningStatusDto.getPlanningStatusName());
        planningStatusRepository.save(status);
    }

    @Override
    public PlanningStatusDto findByPlanningStatusName(String planningStatusName) {
        PlanningStatusDto planningStatus = new PlanningStatusDto();
        if(planningStatusRepository.findByPlanningStatusName(planningStatusName) != null){
            planningStatus = mapToPlanningStatusDto(planningStatusRepository.findByPlanningStatusName(planningStatusName));
        }
        return planningStatus;
    }

    @Override
    public PlanningStatusDto findByPlanningStatusId(long planningStatusId) {
        return mapToPlanningStatusDto(planningStatusRepository.findByPlanningStatusId(planningStatusId));
    }

    @Override
    public PlanningStatusDto mapToPlanningStatusDto(PlanningStatus planningStatus)
    {
        PlanningStatusDto planningStatusDto = new PlanningStatusDto();
        planningStatusDto.setPlanningStatusId(planningStatusDto.getPlanningStatusId());
        planningStatusDto.setPlanningStatusName(planningStatus.getPlanningStatusName());
        return planningStatusDto;
    }

    @Override
    public PlanningStatus mapToPlanningStatus(PlanningStatusDto planningStatusDto)
    {
        PlanningStatus planningStatus = new PlanningStatus();
        planningStatus.setPlanningStatusName(planningStatusDto.getPlanningStatusName());
        return planningStatus;
    }

    private void initializePlanningStatus()
    {
        for (String status : PLANNING_STATUSES)
        {
            PlanningStatus planningStatus = new PlanningStatus();
            planningStatus.setPlanningStatusName(status);
            planningStatusRepository.save(planningStatus);
        }
    }

    @Override
    public void run(String... args) throws Exception
    {
        if(planningStatusRepository.count() == 0)
        {
            initializePlanningStatus();
        }
    }
}
