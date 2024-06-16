package GroepswerkJava.DigitalPlanner.controller;
import GroepswerkJava.DigitalPlanner.dto.LogDetailDto;
import GroepswerkJava.DigitalPlanner.service.LogDetailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@Controller
@RequestMapping("/log-details")
public class LogDetailController {

    @Autowired
    private LogDetailService logDetailService;

    @PostMapping
    public ResponseEntity<LogDetailDto> saveLogDetail(@RequestBody LogDetailDto logDetailDto) {

        logDetailService.saveLogDetail(logDetailDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(logDetailDto);
    }

    @GetMapping("/{id}")
    public ResponseEntity<LogDetailDto> findById(@PathVariable Long logDetailId) {

        LogDetailDto logDetailDto = logDetailService.findById(logDetailId);
        if (logDetailDto != null) {
            return ResponseEntity.ok(logDetailDto);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/allLogDetails")
    public ResponseEntity<List<LogDetailDto>> getAllLogDetails() {

        List<LogDetailDto> logDetailDtoList = logDetailService.findAllLogDetails();
        return ResponseEntity.ok(logDetailDtoList);
    }

    @PutMapping("/{id}")
    public ResponseEntity<LogDetailDto> updateLogDetail(@PathVariable Long logDetailId, @RequestBody LogDetailDto logDetailDto) {

        LogDetailDto updatedLogDetailDto = logDetailService.updateLogDetail(logDetailId, logDetailDto);
        if (updatedLogDetailDto != null) {
            return ResponseEntity.ok(updatedLogDetailDto);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteLogDetail(@PathVariable Long logDetailId) {

        logDetailService.deleteLogDetail(logDetailId);
        return ResponseEntity.noContent().build();
    }
}
