package com.project.barberreservation.controller.impl;

import com.project.barberreservation.controller.IReviewController;
import com.project.barberreservation.dto.request.ReviewRequest;
import com.project.barberreservation.dto.response.ReviewResponse;
import com.project.barberreservation.service.IReviewService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class ReviewControllerImpl implements IReviewController {

    private final IReviewService reviewService;

    @PostMapping("/customer/give-review/{id}")
    @Override
    public ReviewResponse giveReview(@RequestBody ReviewRequest reviewRequest, @PathVariable(name = "id") Long toMasterId) {
        return reviewService.giveReview(reviewRequest, toMasterId);
    }
}
