package GroepswerkJava.DigitalPlanner.service;

import GroepswerkJava.DigitalPlanner.dto.PlanningDetailDto;
import GroepswerkJava.DigitalPlanner.model.PlanningDetail;
import org.springframework.stereotype.Service;

public interface PlanningDetailService {

    void save(PlanningDetail planningDetail);

    PlanningDetail get(long id);

    boolean assignOpenShift(Long shiftId, String email);

    PlanningDetail getById(Long planningDetailId);

//    void deleteById(Long id);
}
