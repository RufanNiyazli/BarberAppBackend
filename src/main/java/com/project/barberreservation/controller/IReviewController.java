package com.project.barberreservation.controller;

import com.project.barberreservation.dto.request.ReviewRequest;
import com.project.barberreservation.dto.response.ReviewResponse;

public interface IReviewController {
    public ReviewResponse giveReview(ReviewRequest reviewRequest, Long toBarberId);
}
