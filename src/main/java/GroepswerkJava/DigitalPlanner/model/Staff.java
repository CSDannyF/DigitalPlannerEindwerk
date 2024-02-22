package GroepswerkJava.DigitalPlanner.model;

import jakarta.persistence.*;

@Entity
@Table(name = "staff")
public class Staff {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long staffId;

    @ManyToOne
    @JoinColumn(name = "staffFunctionId", nullable = false)
    private StaffFunction staffFunction;

    @ManyToOne
    @JoinColumn(name = "departmentId", nullable = false)
    private Department department;

    @ManyToOne
    @JoinColumn(name = "companyId", nullable = false)
    private Company company;

    @Column(name = "staff_name", nullable = false)
    private String staffName;

    @Column(name = "staff_details", nullable = false)
    private String staffDetails;

    @ManyToOne
    @JoinColumn(name = "contract_type", nullable = false)
    private ContractType contractType;

    @ManyToOne
    @JoinColumn(name = "statuteId", nullable = false)
    private Statute statute;

    @ManyToOne
    @JoinColumn(name = "personResonsibleId", nullable = false)
    private Staff personResonsible;

    @OneToOne(mappedBy = "staff")
    private UserLogin userLogin;

    public long getStaffId() {
        return staffId;
    }
    public StaffFunction getStaffFunction() {
        return staffFunction;
    }

    public void setStaffFunction(StaffFunction staffFunction) {
        this.staffFunction = staffFunction;
    }

    public Department getDepartment() {
        return department;
    }

    public void setDepartment(Department department) {
        this.department = department;
    }

    public Company getCompany() {
        return company;
    }

    public void setCompany(Company company) {
        this.company = company;
    }

    public String getStaffName() {
        return staffName;
    }

    public void setStaffName(String staffName) {
        this.staffName = staffName;
    }

    public String getStaffDetails() {
        return staffDetails;
    }

    public void setStaffDetails(String staffDetails) {
        this.staffDetails = staffDetails;
    }

    public ContractType getContractType() {
        return contractType;
    }

    public void setContractType(ContractType contractType) {
        this.contractType = contractType;
    }

    public Statute getStatute() {
        return statute;
    }

    public void setStatute(Statute statute) {
        this.statute = statute;
    }

    public Staff getPersonResonsible() {
        return personResonsible;
    }

    public void setPersonResonsible(Staff personResonsible) {
        this.personResonsible = personResonsible;
    }

    public UserLogin getUserLogin() {
        return userLogin;
    }

    public void setUserLogin(UserLogin userLogin) {
        this.userLogin = userLogin;
    }
}
