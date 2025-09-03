package com.project.barberreservation.controller.impl;

import com.fasterxml.jackson.databind.JsonMappingException;
import com.project.barberreservation.controller.IScheduleController;
import com.project.barberreservation.dto.request.ScheduleRequest;
import com.project.barberreservation.dto.response.ScheduleResponse;
import com.project.barberreservation.service.IScheduleService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequiredArgsConstructor
public class ScheduleControllerImpl implements IScheduleController {
    private final IScheduleService scheduleService;

    @PostMapping("/master/create-schedule")
    @Override
    public ScheduleResponse createSchedule(@RequestBody ScheduleRequest scheduleRequest) {
        return scheduleService.createSchedule(scheduleRequest);
    }

    @Override
    @PatchMapping("/master/update-schedule/{id}")
    public ScheduleResponse updateSchedule(@PathVariable(name = "id") Long scheduleId, @RequestBody Map<String, Object> updates) throws JsonMappingException {
        return scheduleService.updateSchedule(scheduleId, updates);
    }

    @Override
    @DeleteMapping("/master/delete-schedule/{id}")
    public void deleteSchedule(@PathVariable(name = "id") Long scheduleId) {
        scheduleService.deleteSchedule(scheduleId);

    }
}
