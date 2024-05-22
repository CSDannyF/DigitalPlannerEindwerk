package GroepswerkJava.DigitalPlanner.PlanningGenerator;
import GroepswerkJava.DigitalPlanner.dto.LogDetailDto;
import GroepswerkJava.DigitalPlanner.model.Planning;
import GroepswerkJava.DigitalPlanner.service.LogDetailService;
import GroepswerkJava.DigitalPlanner.service.LogService;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Response {
    private List<Planning> planningList;
    private List<LogDetailDto> logging;

    @Autowired
    private LogService logService;

    @Autowired
    private LogDetailService logDetailService;

    public void addLogMessage(LogDetailDto logDetailDto) {
        this.logging.add(logDetailDto);
    }

    public void addLogMessages(List<LogDetailDto> logDetailDtos) {
        this.logging.addAll(logDetailDtos);
    }

    //todo PlanningDto?
    public void addPlanning(Planning planning) {
        this.planningList.add(planning);
    }

    //todo PlanningDto?
    public void addPlannings(List<Planning> plannings) {
        this.planningList.addAll(plannings);
    }

    public void clearLogs() {
        this.logging.clear();
    }

    public void saveLogsToDatabase() {
        for (LogDetailDto logDetailDto: logging) {
            logDetailService.saveLogDetail(logDetailDto);
        }
    }
}