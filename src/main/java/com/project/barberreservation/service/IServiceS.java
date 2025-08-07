package com.project.barberreservation.service;

import com.fasterxml.jackson.databind.JsonMappingException;
import com.project.barberreservation.dto.request.ServiceRequest;
import com.project.barberreservation.dto.response.ServiceResponse;

import java.util.List;
import java.util.Map;

public interface IServiceS {
    public ServiceResponse createService(ServiceRequest serviceRequest);

    public ServiceResponse updateService(Map<String, Object> updates) throws JsonMappingException;

    public void deleteService(Long id);


    public ServiceResponse readService(Long serviceID);


}
