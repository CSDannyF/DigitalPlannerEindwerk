package GroepswerkJava.DigitalPlanner.dto;
import GroepswerkJava.DigitalPlanner.model.LogDetail;
import GroepswerkJava.DigitalPlanner.model.Planning;
import GroepswerkJava.DigitalPlanner.model.Staff;
import GroepswerkJava.DigitalPlanner.model.UserLogin;
import jakarta.validation.constraints.NotEmpty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.time.LocalDate;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class LogDto {
    private long logId;
    @NotEmpty
    private String action;
    @NotEmpty
    private LocalDate plannedDate;
    private UserLogin userLogin;
    private Planning planning;
    private Staff plannedByStaff;
    private List<LogDetail> logDetails;
}