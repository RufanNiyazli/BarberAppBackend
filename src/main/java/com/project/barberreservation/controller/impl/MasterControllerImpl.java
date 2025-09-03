package com.project.barberreservation.controller.impl;

import com.project.barberreservation.controller.IMasterController;
import com.project.barberreservation.dto.response.MasterDetailedResponse;
import com.project.barberreservation.dto.response.MasterResponse;
import com.project.barberreservation.service.IMasterService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Map;

@RestController
@RequiredArgsConstructor
public class MasterControllerImpl implements IMasterController {

    private final IMasterService masterService;

    @Override
    @GetMapping("/public/get-masters")
    public List<MasterResponse> getAllMasters() {
        return masterService.readAllMasters();
    }

    @Override
    @GetMapping("/public/get-master/{id}")
    public MasterDetailedResponse getMasterById(@PathVariable(name = "id") Long id) {
        return masterService.readMasterById(id);
    }

    @Override
    @GetMapping("/master/profile")
    public MasterDetailedResponse readMasterProfileForOwnProfile() {
        return masterService.readMasterProfileForOwnProfile();
    }

    @Override
    @PatchMapping(value = "/master/update-masterProfile/", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public MasterDetailedResponse updateMasterProfile(
            @RequestPart(required = false) Map<String, Object> updates,
            @RequestPart(required = false) MultipartFile profilePhoto,
            @RequestPart(required = false) MultipartFile[] galleryPhotos
    ) throws IOException {
        return masterService.updateMasterProfile(updates, profilePhoto, galleryPhotos);
    }

}
