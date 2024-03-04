package GroepswerkJava.DigitalPlanner.model;

import jakarta.persistence.*;

import java.sql.Time;

@Entity
@Table(name = "planning_detail")
public class PlanningDetail {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long planningDetailId;

    //TODO PlanningId moet denk ik niet uniek zijn. (planningDetailId 1 geeft werktijd van één staffLid,
    // planningDetailId 2 is dan werktijd van staffLid 2, dit kan op dezelfde dag zijn en dus dezelfde planningId hebben?

    @ManyToOne
    @JoinColumn(name = "planningId", nullable = false, unique = true)
    private Planning planning;

    @ManyToOne
    @JoinColumn(name = "staffId", nullable = false, unique = true)
    private Staff staff;

    @ManyToOne
    @JoinColumn(name = "availabilityId", nullable = false)
    private Availability availability;

    @Column(name = "start_time")
    private Time startTime;

    @Column(name = "end_time")
    private Time endTime;

    public long getPlanningDetailId() {
        return planningDetailId;
    }

    public Planning getPlanning() {
        return planning;
    }

    public void setPlanning(Planning planning) {
        this.planning = planning;
    }

    public Staff getStaff() {
        return staff;
    }

    public void setStaff(Staff staff) {
        this.staff = staff;
    }

    public Availability getAvailability() {
        return availability;
    }

    public void setAvailability(Availability availability) {
        this.availability = availability;
    }

    public Time getStartTime() {
        return startTime;
    }

    public void setStartTime(Time startTime) {
        this.startTime = startTime;
    }

    public Time getEndTime() {
        return endTime;
    }

    public void setEndTime(Time endTime) {
        this.endTime = endTime;
    }
}
