package com.project.barberreservation.controller;

import com.fasterxml.jackson.databind.JsonMappingException;
import com.project.barberreservation.dto.response.BarberDetailedResponse;
import com.project.barberreservation.dto.response.BarberResponse;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Map;

public interface IBarberController {

    public List<BarberResponse> getAllBarbers();

    public BarberDetailedResponse getBarberById(Long id);

    public BarberDetailedResponse readBarberProfileForOwnProfile();

    public BarberDetailedResponse updateBarberProfile(Map<String, Object> updates,
                                                      MultipartFile profilePhoto,
                                                      MultipartFile[] galleryPhotos) throws IOException;

}
