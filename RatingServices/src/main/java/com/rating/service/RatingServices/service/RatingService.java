package com.rating.service.RatingServices.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.rating.service.RatingServices.entities.Rating;

@Service
public interface RatingService {
	//create
		Rating create(Rating rating);

		//get all ratings
		List<Rating> getRatings();

		//get all by userId

		List<Rating> getRatingsByUserId(String userId);
		//get all by HotelId

		List<Rating> getRatingsByHotelId(String hotelId);

}
