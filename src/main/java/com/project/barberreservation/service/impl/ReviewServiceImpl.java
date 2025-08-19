package com.project.barberreservation.service.impl;

import com.project.barberreservation.dto.request.ReviewRequest;
import com.project.barberreservation.dto.response.ReviewResponse;
import com.project.barberreservation.entity.Barber;
import com.project.barberreservation.entity.Review;
import com.project.barberreservation.entity.User;
import com.project.barberreservation.repository.BarberRepository;
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
    private final BarberRepository barberRepository;
    private final UserRepository userRepository;

    @Override
    public ReviewResponse giveReview(ReviewRequest reviewRequest, Long toBarberId) {
        Optional<Barber> optionalBarber = barberRepository.findById(toBarberId);
        if (optionalBarber.isEmpty()) {
            throw new RuntimeException("Barber not Found!");
        }
        Barber barber = optionalBarber.get();


        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        User user = userRepository.findUserByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));

        Optional<User> optionalCustomer = userRepository.findById(user.getId());
        if (optionalCustomer.isEmpty()) {
            throw new RuntimeException("Barber not found!");
        }
        User customer = optionalCustomer.get();
        validateReviewInput(reviewRequest);

        Review review = Review.builder()
                .barber(barber)
                .comment(reviewRequest.getComment())
                .createdAt(LocalDateTime.now())
                .customer(customer)
                .rating(reviewRequest.getRating())
                .build();

        Review savedReview = reviewRepository.save(review);

        Double averageRating = reviewRepository.findAverageRatingByBarberId(barber.getId());
        barber.setRating(averageRating);
        barberRepository.save(barber);

        return ReviewResponse.builder()
                .id(savedReview.getId())
                .barberName(savedReview.getBarber().getName())
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
