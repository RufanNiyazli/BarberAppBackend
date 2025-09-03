package com.project.barberreservation.controller;

import com.project.barberreservation.dto.response.MasterDetailedResponse;
import com.project.barberreservation.dto.response.MasterResponse;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Map;

public interface IMasterController {

    public List<MasterResponse> getAllMasters();

    public MasterDetailedResponse getMasterById(Long id);

    public MasterDetailedResponse readMasterProfileForOwnProfile();

    public MasterDetailedResponse updateMasterProfile(Map<String, Object> updates,
                                                      MultipartFile profilePhoto,
                                                      MultipartFile[] galleryPhotos) throws IOException;

}
