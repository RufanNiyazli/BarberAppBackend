package com.project.barberreservation.service.impl;

import com.project.barberreservation.dto.request.ReviewRequest;
import com.project.barberreservation.dto.response.ReviewResponse;
import com.project.barberreservation.entity.Master;
import com.project.barberreservation.entity.Review;
import com.project.barberreservation.entity.User;
import com.project.barberreservation.repository.MasterRepository;
import com.project.barberreservation.repository.ReviewRepository;
import com.project.barberreservation.repository.UserRepository;
import com.project.barberreservation.service.IReviewService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ReviewServiceImpl implements IReviewService {
    private final ReviewRepository reviewRepository;
    private final MasterRepository masterRepository;
    private final UserRepository userRepository;

    @Override
    public ReviewResponse giveReview(ReviewRequest reviewRequest, Long toMasterId) {
        Optional<Master> optionalMaster = masterRepository.findById(toMasterId);
        if (optionalMaster.isEmpty()) {
            throw new RuntimeException("Master not Found!");
        }
        Master master = optionalMaster.get();


        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        User user = userRepository.findUserByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));

        Optional<User> optionalCustomer = userRepository.findById(user.getId());
        if (optionalCustomer.isEmpty()) {
            throw new RuntimeException("Master not found!");
        }
        User customer = optionalCustomer.get();
        validateReviewInput(reviewRequest);

        Review review = Review.builder()
                .master(master)
                .comment(reviewRequest.getComment())
                .createdAt(LocalDateTime.now())
                .customer(customer)
                .rating(reviewRequest.getRating())
                .build();

        Review savedReview = reviewRepository.save(review);

        Double averageRating = reviewRepository.findAverageRatingByMasterId(master.getId());
        master.setRating(averageRating);
        masterRepository.save(master);

        return ReviewResponse.builder()
                .id(savedReview.getId())
                .masterName(savedReview.getMaster().getName())
                .comment(savedReview.getComment())
                .customerName(savedReview.getCustomer().getUsername())
                .rating(savedReview.getRating())
                .createdAt(savedReview.getCreatedAt())
                .build();
    }

    private void validateReviewInput(ReviewRequest reviewRequest) {
        boolean hasRating = reviewRequest.getRating() != null;
        boolean hasComment = reviewRequest.getComment() != null;
        if (!hasRating && hasComment) {
            throw new IllegalArgumentException("If Comment have , Rating will have to");

        }
        if (!hasRating && !hasComment) {
            throw new IllegalArgumentException("Blank Review dont allow");
        }
    }
}
