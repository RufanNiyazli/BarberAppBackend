package com.project.barberreservation.service;

import com.project.barberreservation.dto.request.ReviewRequest;
import com.project.barberreservation.dto.response.ReviewResponse;

public interface IReviewService {

    public ReviewResponse giveReview(ReviewRequest reviewRequest, Long toMasterId);


}
