package GroepswerkJava.DigitalPlanner.service;

import GroepswerkJava.DigitalPlanner.model.PlanningDetail;
import org.springframework.stereotype.Service;

public interface PlanningDetailService {

    void save(PlanningDetail planningDetail);

    PlanningDetail get(long id);
}
