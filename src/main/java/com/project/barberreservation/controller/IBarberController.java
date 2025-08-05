package com.project.barberreservation.controller;

import com.fasterxml.jackson.databind.JsonMappingException;
import com.project.barberreservation.dto.BarberDetailDTO;
import com.project.barberreservation.dto.BarberResponse;

import java.util.List;
import java.util.Map;

public interface IBarberController {

    public List<BarberResponse> getAllBarbers();

    public BarberDetailDTO getBarberById(Long id);

    public BarberDetailDTO updateBarberProfile(Map<String, Object> updates) throws JsonMappingException;

}
