package GroepswerkJava.DigitalPlanner.service;

import GroepswerkJava.DigitalPlanner.dto.LogDto;

import java.util.List;

public interface LogService {
    void saveLog(LogDto logDto);

    LogDto findById(Long logId);

    List<LogDto> findAllLogs();

    LogDto updateLog(Long logId, LogDto logDto);

    void deleteLog(Long logId);
}