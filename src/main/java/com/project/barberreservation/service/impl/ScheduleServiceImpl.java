package com.project.barberreservation.service.impl;

import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.project.barberreservation.dto.request.ScheduleRequest;
import com.project.barberreservation.dto.response.ScheduleResponse;
import com.project.barberreservation.entity.Master;
import com.project.barberreservation.entity.Schedule;
import com.project.barberreservation.entity.User;
import com.project.barberreservation.repository.MasterRepository;
import com.project.barberreservation.repository.ScheduleRepository;
import com.project.barberreservation.repository.UserRepository;
import com.project.barberreservation.service.IScheduleService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.Optional;

@Service
@RequiredArgsConstructor

public class ScheduleServiceImpl implements IScheduleService {

    private final ScheduleRepository scheduleRepository;
    private final MasterRepository masterRepository;
    private final UserRepository userRepository;
    private final ObjectMapper objectMapper;

    @Override
    public ScheduleResponse createSchedule(ScheduleRequest scheduleRequest) {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        User user = userRepository.findUserByEmail(email).orElseThrow(() -> new RuntimeException("User not found"));
        Optional<Master> optional = masterRepository.findByUserId(user.getId());
        if (optional.isEmpty()) {
            throw new RuntimeException("Master not found!");
        }
        Master master = optional.get();
        Schedule schedule = Schedule.builder().createdAt(LocalDateTime.now()).master(master).dayOfWeek(scheduleRequest.getDayOfWeek()).startTime(scheduleRequest.getStartTime()).endTime(scheduleRequest.getEndTime()).updatedAt(LocalDateTime.now()).build();

        Schedule dbSchedule = scheduleRepository.save(schedule);
        return ScheduleResponse.builder().dayOfWeek(dbSchedule.getDayOfWeek()).endTime(dbSchedule.getEndTime()).startTime(dbSchedule.getStartTime()).build();
    }

    @Override
    public ScheduleResponse updateSchedule(Long scheduleId, Map<String, Object> updates) throws JsonMappingException {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        User user = userRepository.findUserByEmail(email).orElseThrow(() -> new RuntimeException("User not found"));
        Optional<Master> optional = masterRepository.findByUserId(user.getId());
        if (optional.isEmpty()) {
            throw new RuntimeException("Master not found!");
        }
        Master master = optional.get();

        Optional<Schedule> optionalSchedule = scheduleRepository.findByIdAndMaster(scheduleId, master);
        Schedule schedule = optionalSchedule.orElseThrow(() -> new RuntimeException("Schedule not found for this master"));
        objectMapper.updateValue(schedule, updates);
        schedule.setUpdatedAt(LocalDateTime.now());
        Schedule dbSchedule = scheduleRepository.save(schedule);

        return ScheduleResponse.builder().endTime(dbSchedule.getEndTime()).dayOfWeek(dbSchedule.getDayOfWeek()).startTime(dbSchedule.getStartTime())


                .build();
    }

    @Override
    public void deleteSchedule(Long scheduleId) {
        Schedule schedule = scheduleRepository.findById(scheduleId).orElseThrow(() -> new RuntimeException("Schedule not found!"));

        scheduleRepository.delete(schedule);

    }
}
