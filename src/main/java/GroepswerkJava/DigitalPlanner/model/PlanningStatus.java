package GroepswerkJava.DigitalPlanner.model;

import jakarta.persistence.*;

@Entity
@Table(name ="planningStatus")
public class PlanningStatus {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long planningStatusId;

    @Column(nullable = false, unique = true, name = "planning_status_name")
    private String planningStatusName;

    public long getPlanningStatusId() {
        return planningStatusId;
    }


    public String getPlanningStatusName() {
        return planningStatusName;
    }

    public void setPlanningStatusName(String planningStatusName) {
        this.planningStatusName = planningStatusName;
    }
}
