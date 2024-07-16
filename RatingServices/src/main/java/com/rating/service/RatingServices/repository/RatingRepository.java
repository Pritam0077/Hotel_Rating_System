package com.rating.service.RatingServices.repository;

import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.rating.service.RatingServices.entities.Rating;

public interface RatingRepository extends MongoRepository<Rating, String> {

	// custom finder methods
	List<Rating> findByUserId(String userId);
	List<Rating> findByHotelId(String hotelId);

}
