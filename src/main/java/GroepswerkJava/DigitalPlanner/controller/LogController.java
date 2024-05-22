package GroepswerkJava.DigitalPlanner.controller;

import GroepswerkJava.DigitalPlanner.dto.LogDto;
import GroepswerkJava.DigitalPlanner.service.LogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@Controller
@RequestMapping("/log")
public class LogController {

    @Autowired
    private LogService logService;

    @PostMapping
    public ResponseEntity<Void> saveLog(@RequestBody LogDto logDto) {
        logService.saveLog(logDto);
        return ResponseEntity.status(201).build();
    }

    @GetMapping("/{id}")
    public ResponseEntity<LogDto> findById(@PathVariable Long logId) {
        LogDto logDto = logService.findById(logId);
        return ResponseEntity.ok(logDto);
    }

    @GetMapping("/allLogs")
    public ResponseEntity<List<LogDto>> findAllLogs() {
        List<LogDto> logDtoList = logService.findAllLogs();
        return ResponseEntity.ok(logDtoList);
    }

    @PutMapping("{id}")
    public ResponseEntity<LogDto> updateLog(@PathVariable Long logId, @RequestBody LogDto logDto) {
        LogDto updatedLog = logService.updateLog(logId, logDto);
        return ResponseEntity.ok(updatedLog);
    }

    @DeleteMapping("{id}")
    public ResponseEntity<LogDto> deleteLog(@PathVariable Long logId) {
        logService.deleteLog(logId);
        return ResponseEntity.noContent().build();
    }
}
