package com.project.barberreservation.service;

import com.fasterxml.jackson.databind.JsonMappingException;
import com.project.barberreservation.dto.response.BarberDetailedResponse;
import com.project.barberreservation.dto.response.BarberResponse;

import java.util.List;
import java.util.Map;

public interface IBarberService {

    public List<BarberResponse> getAllBarbers();

    public BarberDetailedResponse getBarberById(Long id);

    public BarberDetailedResponse updateBarberProfile(Map<String, Object> updates) throws JsonMappingException;
}
