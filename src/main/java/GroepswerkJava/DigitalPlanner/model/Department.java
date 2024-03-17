package GroepswerkJava.DigitalPlanner.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "department")
public class Department {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long departmentId;

    @Column(name = "departmentName", unique = true, nullable = false)
    private String departmentName;

    @ManyToOne
    @JoinColumn(name = "departmentId", nullable=false)
    private Staff staff;
}
