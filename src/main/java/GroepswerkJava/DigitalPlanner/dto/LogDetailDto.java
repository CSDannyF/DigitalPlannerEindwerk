package GroepswerkJava.DigitalPlanner.dto;
import GroepswerkJava.DigitalPlanner.model.Log;
import GroepswerkJava.DigitalPlanner.model.LogType;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
public class LogDetailDto {
    private long logDetailId;
    private Log log;
    private LogType logType;
    private String message;
    private LocalDate timestamp;
}