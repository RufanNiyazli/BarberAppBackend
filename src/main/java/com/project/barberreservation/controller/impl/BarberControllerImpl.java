package com.project.barberreservation.controller.impl;

import com.fasterxml.jackson.databind.JsonMappingException;
import com.project.barberreservation.controller.IBarberController;
import com.project.barberreservation.dto.response.BarberDetailedResponse;
import com.project.barberreservation.dto.response.BarberResponse;
import com.project.barberreservation.service.IBarberService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Map;

@RestController
@RequiredArgsConstructor
public class BarberControllerImpl implements IBarberController {

    private final IBarberService barberService;

    @Override
    @GetMapping("/public/get-barbers")
    public List<BarberResponse> getAllBarbers() {
        return barberService.readAllBarbers();
    }

    @Override
    @GetMapping("/public/get-barber/{id}")
    public BarberDetailedResponse getBarberById(@PathVariable(name = "id") Long id) {
        return barberService.readBarberById(id);
    }

    @Override
    @GetMapping("/barber/profile")
    public BarberDetailedResponse readBarberProfileForOwnProfile() {
        return barberService.readBarberProfileForOwnProfile();
    }

    @Override
    @PatchMapping(value = "/barber/update-barberProfile/", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public BarberDetailedResponse updateBarberProfile(
            @RequestPart(required = false) Map<String, Object> updates,
            @RequestPart(required = false) MultipartFile profilePhoto,
            @RequestPart(required = false) MultipartFile[] galleryPhotos
    ) throws IOException {
        return barberService.updateBarberProfile(updates, profilePhoto, galleryPhotos);
    }

}
