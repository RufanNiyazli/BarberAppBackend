package com.project.barberreservation.controller;

import com.fasterxml.jackson.databind.JsonMappingException;
import com.project.barberreservation.dto.request.ScheduleRequest;
import com.project.barberreservation.dto.response.ScheduleResponse;

import java.util.Map;

public interface IScheduleController {

    public ScheduleResponse createSchedule(ScheduleRequest scheduleRequest);

    public ScheduleResponse updateSchedule(Long scheduleId, Map<String, Object> updates) throws JsonMappingException;

    public void deleteSchedule(Long scheduleId);
}
