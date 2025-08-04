package com.project.barberreservation.service;

import com.fasterxml.jackson.databind.JsonMappingException;
import com.project.barberreservation.dto.BarberDetailDTO;
import com.project.barberreservation.dto.BarberResponse;

import java.util.List;
import java.util.Map;

public interface IBarberService {

    public List<BarberResponse> getAllBarbers();

    public BarberDetailDTO getBarberById(Long id);

    public BarberDetailDTO updateBarberProfile(Long barberId, Map<String, Object> updates, Long currentUserId) throws JsonMappingException;
}
