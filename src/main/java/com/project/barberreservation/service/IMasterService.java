package com.project.barberreservation.service;

import com.project.barberreservation.dto.response.MasterDetailedResponse;
import com.project.barberreservation.dto.response.BarberResponse;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Map;

public interface IMasterService {

    public List<BarberResponse> readAllBarbers();

    public MasterDetailedResponse readBarberById(Long id);

    public MasterDetailedResponse readBarberProfileForOwnProfile();

    public MasterDetailedResponse updateBarberProfile(Map<String, Object> updates,
                                                      MultipartFile profilePhoto,
                                                      MultipartFile[] galleryPhotos) throws IOException;
}
