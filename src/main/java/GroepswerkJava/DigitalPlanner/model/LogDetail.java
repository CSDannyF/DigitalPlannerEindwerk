package GroepswerkJava.DigitalPlanner.model;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "logDetail")
public class LogDetail {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long logDetailId;

    @ManyToOne
    @JoinColumn(name = "logId")
    private Log log;

    // EnumType.STRING to save logType as String in database
    @Enumerated(EnumType.STRING)
    @Column(name = "logType", nullable = false)
    private LogType logType;

    @Column(name = "message", nullable = false)
    private String message;

    @Column(name = "timestamp", nullable = false)
    private LocalDate timestamp;
}