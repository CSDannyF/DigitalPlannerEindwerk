package GroepswerkJava.DigitalPlanner.model;

import jakarta.persistence.*;

@Entity
@Table(name = "fixed_day_off")
public class FixedDayOff {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long fixedDayOffId;

    @ManyToOne
    @JoinColumn(name = "staff_id", unique = true, nullable = false)
    private Staff staff;

    @ManyToOne
    @JoinColumn(name = "days_of_week_id", unique = true, nullable = false)
    private DaysOfWeek daysOfWeek;

    public long getFixedDayOffId() {
        return fixedDayOffId;
    }
    public Staff getStaff() {
        return staff;
    }

    public void setStaff(Staff staff) {
        this.staff = staff;
    }

    public DaysOfWeek getDaysOfWeek() {
        return daysOfWeek;
    }

    public void setDaysOfWeek(DaysOfWeek daysOfWeek) {
        this.daysOfWeek = daysOfWeek;
    }
}
