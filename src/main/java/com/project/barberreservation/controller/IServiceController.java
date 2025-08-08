package com.project.barberreservation.controller;

import com.fasterxml.jackson.databind.JsonMappingException;
import com.project.barberreservation.dto.request.ServiceRequest;
import com.project.barberreservation.dto.response.ServiceResponse;

import java.util.List;
import java.util.Map;

public interface IServiceController {
    public ServiceResponse createService(ServiceRequest serviceRequest);

    public ServiceResponse updateService(Map<String, Object> updates, Long id) throws JsonMappingException;

    public void deleteService(Long id);


    public ServiceResponse readService(Long serviceID);

    public List<ServiceResponse> readServices();
}
