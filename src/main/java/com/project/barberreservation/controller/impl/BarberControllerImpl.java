package com.project.barberreservation.controller.impl;

import com.fasterxml.jackson.databind.JsonMappingException;
import com.project.barberreservation.controller.IBarberController;
import com.project.barberreservation.dto.BarberDetailDTO;
import com.project.barberreservation.dto.BarberResponse;
import com.project.barberreservation.service.IBarberService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequiredArgsConstructor

public class BarberControllerImpl implements IBarberController {

    private final IBarberService barberService;

    @Override
    @GetMapping({"/public/get-barbers", "/barber/get-barbers"})
    public List<BarberResponse> getAllBarbers() {
        return barberService.getAllBarbers();
    }

    @Override
    @GetMapping("/public/get-barber/{id}")
    public BarberDetailDTO getBarberById(@PathVariable(name = "id") Long id) {
        return barberService.getBarberById(id);
    }

    @Override
    @PatchMapping("/barber/update-barberProfile/")

    public BarberDetailDTO updateBarberProfile(@RequestBody Map<String, Object> updates) throws JsonMappingException {
        return barberService.updateBarberProfile(updates);
    }
}
