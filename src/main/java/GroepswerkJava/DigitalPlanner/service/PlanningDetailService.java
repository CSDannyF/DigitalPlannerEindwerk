package GroepswerkJava.DigitalPlanner.service;

import GroepswerkJava.DigitalPlanner.dto.PlanningDetailDto;
import GroepswerkJava.DigitalPlanner.model.PlanningDetail;

public interface PlanningDetailService {

    PlanningDetailDto save(PlanningDetail planningDetail);

    PlanningDetail get(long id);

    boolean assignOpenShift(Long shiftId, String email);

    PlanningDetail getById(Long planningDetailId);

}
