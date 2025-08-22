package com.project.barberreservation.service;

import com.fasterxml.jackson.databind.JsonMappingException;
import com.project.barberreservation.dto.response.BarberDetailedResponse;
import com.project.barberreservation.dto.response.BarberResponse;
import com.project.barberreservation.entity.Barber;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Map;

public interface IBarberService {

    public List<BarberResponse> readAllBarbers();

    public BarberDetailedResponse readBarberById(Long id);

    public BarberDetailedResponse readBarberProfileForOwnProfile();

    public BarberDetailedResponse updateBarberProfile(Map<String, Object> updates,
                                                      MultipartFile profilePhoto,
                                                      MultipartFile[] galleryPhotos) throws IOException;
}
