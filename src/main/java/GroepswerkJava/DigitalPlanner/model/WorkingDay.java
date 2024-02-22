package GroepswerkJava.DigitalPlanner.model;

import jakarta.persistence.*;

import java.sql.Date;
import java.sql.Time;

@Entity
@Table(name ="WorkingDay")
public class WorkingDay {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long workingDayId;

    @ManyToOne
    @JoinColumn(name="planning_id", nullable=false,unique = true)
    private Planning planning;

    @ManyToOne
    @JoinColumn(name="company_id", nullable=false,unique = true)
    private Company companyId;

    @Column(nullable = false, unique = true, name = "date")
    private Date date;

    @Column(nullable = false, name = "open")
    private boolean open;

    @Column(nullable = false, name = "start_time")
    private Time startTime;

    @Column(nullable = false, name = "end_time")
    private Time endTime;

    public long getWorkingDayId() {
        return workingDayId;
    }

    public Planning getPlanning() {
        return planning;
    }

    public void setPlanning(Planning planning) {
        this.planning = planning;
    }

    public Company getCompanyId() {
        return companyId;
    }

    public void setCompanyId(Company companyId) {
        this.companyId = companyId;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public boolean isOpen() {
        return open;
    }

    public void setOpen(boolean open) {
        this.open = open;
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
