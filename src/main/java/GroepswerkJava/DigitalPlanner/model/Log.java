package GroepswerkJava.DigitalPlanner.model;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.time.LocalDate;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "log")
public class Log {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long logId;

    // User voorlopig niet gebruiken, auto link tussen user & staff nog niet gemaakt
    @ManyToOne
    @JoinColumn(name = "userLoginId")
    private UserLogin userLogin;

    // Created planning, change an existing planning etc.
    @Column(name = "action", nullable = false)
    private String action;

    @ManyToOne
    @JoinColumn(name = "planningId")
    private Planning planning;

    @Column(name = "plannedDate")
    private LocalDate plannedDate;

    @OneToMany(mappedBy = "log", cascade = CascadeType.ALL)
    private List<LogDetail> logDetails;
}

