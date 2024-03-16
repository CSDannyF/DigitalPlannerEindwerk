package GroepswerkJava.DigitalPlanner.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name ="company")
public class Company {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long companyId;

    @Column(nullable = false, unique = true, name = "company_name")
    private String name;

    @OneToMany(mappedBy = "company", cascade = CascadeType.ALL)
    private List<Staff> staff;
}
