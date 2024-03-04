package GroepswerkJava.DigitalPlanner.model;

import jakarta.persistence.*;

import java.util.Set;

@Entity
@Table(name ="planning")
public class Planning {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long planningId;

    //TODO waarom set van workingDays? moet dit planningDetails zijn?

    @OneToMany(mappedBy = "planning")
    private Set<WorkingDay> workingDays;


    //TODO moet dit uniek zijn? Planning met id 1 kan status "volledig ingevuld" hebben,
    // maar ook planningen met andere id's kunnen dezelfde status hebben

    @ManyToOne
    @JoinColumn(name = "planningStatusId", nullable=false,unique = true)
    private PlanningStatus planningStatus;

    public long getPlanningId() {
        return planningId;
    }

    public Set<WorkingDay> getWorkingDays() {
        return workingDays;
    }

    public void setWorkingDays(Set<WorkingDay> workingDays) {
        this.workingDays = workingDays;
    }

    public PlanningStatus getPlanningStatus() {
        return planningStatus;
    }

    public void setPlanningStatus(PlanningStatus planningStatus) {
        this.planningStatus = planningStatus;
    }
}
