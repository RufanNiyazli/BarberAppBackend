package com.project.barberreservation.controller.impl;

import com.fasterxml.jackson.databind.JsonMappingException;
import com.project.barberreservation.controller.IServiceController;
import com.project.barberreservation.dto.request.ServiceRequest;
import com.project.barberreservation.dto.response.ServiceResponse;
import com.project.barberreservation.service.IServiceS;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequiredArgsConstructor
public class ServiceControllerImpl implements IServiceController {
    private final IServiceS serviceS;


    @PostMapping("/master/create-service")
    @Override
    public ServiceResponse createService(@RequestBody ServiceRequest serviceRequest) {
        return serviceS.createService(serviceRequest);
    }

    @Override
    @PatchMapping("/master/update-service/{id}")
    public ServiceResponse updateService(@RequestBody Map<String, Object> updates, @PathVariable(name = "id") Long id) throws JsonMappingException {
        return serviceS.updateService(updates, id);
    }

    @Override
    @DeleteMapping("/master/delete-service/{id}")
    public void deleteService(@PathVariable(name = "id") Long serviceId) {
        serviceS.deleteService(serviceId);

    }

    @Override
    @GetMapping("/public/read-service/{id}")
    public ServiceResponse readService(@PathVariable(name = "id") Long serviceId) {
        return serviceS.readService(serviceId);
    }

    @Override
    @GetMapping("/master/read-services")
    public List<ServiceResponse> readServices() {
        return serviceS.readServices();
    }
}
