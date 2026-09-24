package com.chamil.demo.review.impl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;

import com.chamil.demo.company.Company;
import com.chamil.demo.company.CompanyService;
import com.chamil.demo.review.Review;
import com.chamil.demo.review.ReviewRepository;
import com.chamil.demo.review.ReviewService;

@Service
public class ReviewServiceImpl implements ReviewService {
    private final ReviewRepository reviewRepository;
    private final CompanyService companyService;

    public ReviewServiceImpl(ReviewRepository reviewRepository,
            CompanyService companyService) {
        this.reviewRepository = reviewRepository;
        this.companyService = companyService;
    }

    @Override
    public List<Review> findAllReviewsByCompanyId(Long companyId) {
        return reviewRepository.findByCompanyId(companyId);
    }

    @Override
    public boolean addReview(Long companyId, Review review) {
        Company company = companyService.findById(companyId);
        if (company != null) {
            review.setCompany(company);
            reviewRepository.save(review);
            return true;
        }
        return false;
    }

    @Override
    public Review getReviewById(Long companyId, Long reviewId) {
        List<Review> reviews = reviewRepository.findByCompanyId(companyId);
        return reviews.stream()
                .filter(review -> review.getId().equals(reviewId))
                .findFirst()
                .orElse(null);

    }

    @Override
    public boolean updateReview(Long companyId, Long reviewId, Review updateReview) {
        Review review = getReviewById(companyId, reviewId);
        if (review != null) {
            review.setDescription(updateReview.getDescription());
            review.setRating(updateReview.getRating());
            review.setTitle(updateReview.getTitle());
            reviewRepository.save(review);
            return true;
        }
        return false;
    }

    @Override
    public boolean deleteReview(Long companyId, Long reviewId) {
        Review review = getReviewById(companyId, reviewId);
        if (review != null) {
            reviewRepository.delete(review);
            return true;
        }
        return false;
    }

}
