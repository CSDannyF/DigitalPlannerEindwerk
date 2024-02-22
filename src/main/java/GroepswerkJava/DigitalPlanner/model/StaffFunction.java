package GroepswerkJava.DigitalPlanner.model;

import jakarta.persistence.*;

@Entity
@Table(name = "staff_function")
public class StaffFunction {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long staffFunctionId;

    @Column(name = "staff_function_name", unique = true)
    private String staffFunctionName;

    @Column(name = "staff_function_description", unique = true)
    private String staffFunctionDescription;

    public long getStaffFunctionId() {
        return staffFunctionId;
    }

    public String getStaffFunctionName() {
        return staffFunctionName;
    }

    public void setStaffFunctionName(String staffFunctionName) {
        this.staffFunctionName = staffFunctionName;
    }

    public String getStaffFunctionDescription() {
        return staffFunctionDescription;
    }

    public void setStaffFunctionDescription(String staffFunctionDescription) {
        this.staffFunctionDescription = staffFunctionDescription;
    }
}
