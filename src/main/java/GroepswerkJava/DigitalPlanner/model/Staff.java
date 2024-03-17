package GroepswerkJava.DigitalPlanner.model;

import jakarta.persistence.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Set;

@Entity
@Table(name = "staff")
public class Staff {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long staffId;

    @Column(name = "staffFirstName", nullable = false)
    private String staffFirstName;

    @Column(name = "staffLastName", nullable = false)
    private String staffLastName;

    @Column(name = "birthDate")
    private Date birthDate;

    @Column(name = "addressLine1")
    private String adressLine1;

    @Column(name = "addressLine2")
    private String adressLine2;

    @Column(name = "zipCode")
    private String zipCode;

    @Column(name = "city")
    private String city;

    @Column(name = "phoneNumber")
    private String phoneNumber;

    @Column(name = "startDate")
    private Date startDate;

    @ManyToOne
    @JoinColumn(name = "staffFunctionId")
    private StaffFunction staffFunction;

    @ManyToOne
    @JoinColumn(name = "departmentId")
    private Department department;

    @ManyToOne
    @JoinColumn(name = "companyId", nullable = false)
    private Company company;

    @ManyToOne
    @JoinColumn(name = "contractTypeId", nullable = false)
    private ContractType contractType;

    @ManyToOne
    @JoinColumn(name = "personResponsibleId")
    private Staff personResponsible;

    @OneToOne(mappedBy = "staff", cascade = CascadeType.ALL)
    private UserLogin userLogin;

    @ManyToMany(cascade = {
            CascadeType.PERSIST,
            CascadeType.MERGE
    })
    @JoinTable(
            name = "fixedDaysOff",
            joinColumns = {@JoinColumn(name = "staffId")},
            inverseJoinColumns = {@JoinColumn(name = "daysOfWeekId")}
    )
    private Set<DaysOfWeek> daysOfWeek;

    @OneToMany(mappedBy = "staff", cascade = CascadeType.ALL)
    private List<PlanningDetail> planningDetails;
}