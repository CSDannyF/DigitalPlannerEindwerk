package GroepswerkJava.DigitalPlanner.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.util.*;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "staff")
public class Staff {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long staffId;

    @Column(name = "staffFirstName")
    private String staffFirstName;

    @Column(name = "staffLastName")
    private String staffLastName;

    @Column(name = "birthDate")
    private LocalDate birthDate;

    @Column(name = "addressLine1")
    private String addressLine1;

    @Column(name = "addressLine2")
    private String addressLine2;

    @Column(name = "zipCode")
    private String zipCode;

    @Column(name = "city")
    private String city;

    @Column(name = "phoneNumber")
    private String phoneNumber;

    @Column(name = "startDate")
    private LocalDate startDate;

    @Column(name = "endDate")
    private LocalDate endDate;

    @ManyToOne
    @JoinColumn(name = "functionId")
    private StaffFunction staffFunction;

    @ManyToOne
    @JoinColumn(name = "departmentId")
    private Department department;

    @ManyToOne
    @JoinColumn(name = "companyId")
    private Company company;

    @ManyToOne
    @JoinColumn(name = "contractTypeId")
    private ContractType contractType;

    @ManyToOne(fetch = FetchType.LAZY, optional = true)
    @JoinColumn(name = "personResponsibleId")
    private Staff personResponsible;

    @OneToMany(mappedBy = "personResponsible", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<Staff> staffMembers = new HashSet<Staff>();

    @OneToOne(mappedBy = "staff")
    private UserLogin userLogin;

    @ManyToMany(cascade = {
            //CascadeType.PERSIST,
            CascadeType.MERGE
    })
    @JoinTable(
            name = "fixedDaysOff",
            joinColumns = {@JoinColumn(name = "staffId")},
            inverseJoinColumns = {@JoinColumn(name = "daysOfWeekId")}
    )
    private Set<DaysOffWeek> daysOffWeek;

    @OneToMany(mappedBy = "staff", cascade = CascadeType.ALL)
    private List<PlanningDetail> planningDetails;

    @Column(name = "hoursInDebt")
    private int hoursInDebt;

    @Override
    public String toString() {
        return "Staff{" +
                "staffId=" + staffId +
                ", staffFirstName='" + staffFirstName + '\'' +
                ", staffLastName='" + staffLastName + '\'' +
                '}';
    }
}