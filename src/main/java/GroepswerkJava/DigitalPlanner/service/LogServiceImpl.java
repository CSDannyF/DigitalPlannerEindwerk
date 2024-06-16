package GroepswerkJava.DigitalPlanner.service;

import GroepswerkJava.DigitalPlanner.PlanningGenerator.LogMapper;
import GroepswerkJava.DigitalPlanner.dto.*;
import GroepswerkJava.DigitalPlanner.model.Log;
import GroepswerkJava.DigitalPlanner.repository.LogRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class LogServiceImpl implements LogService {

    @Autowired
    private LogRepository logRepository;

    private final LogMapper logMapper = LogMapper.INSTANCE;

    public LogServiceImpl(LogRepository logRepository) {
        this.logRepository = logRepository;
    }

    @Override
    public void saveLog(LogDto logDto) {
        Log log = logMapper.logDtoToLog(logDto);
        logRepository.save(log);
    }

    @Override
    public LogDto findById(Long logId) {
        return logMapper.logToLogDto(logRepository.findById(logId).orElse(null));
    }

    @Override
    public List<LogDto> findAllLogs() {
        return logRepository.findAll().stream()
                .map(logMapper::logToLogDto)
                .collect(Collectors.toList());
    }

    @Override
    public LogDto updateLog(Long logId, LogDto logDto) {
        if (logRepository.existsById(logId)) {
            Log log = logMapper.logDtoToLog(logDto);
            log.setLogId(logId);
            log = logRepository.save(log);
            return logMapper.logToLogDto(log);
        } else {
            return null;
        }
    }

    @Override
    public void deleteLog(Long logId) {
        if (logRepository.existsById(logId)) {
            logRepository.deleteById(logId);
        } else {
            throw new RuntimeException("Log not found");
        }
    }
}
