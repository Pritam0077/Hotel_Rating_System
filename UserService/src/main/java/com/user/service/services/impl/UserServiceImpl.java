package com.user.service.services.impl;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import org.hibernate.transform.ToListResultTransformer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.user.service.entities.Hotel;
import com.user.service.entities.Rating;
import com.user.service.entities.User;
import com.user.service.exceptions.ResourceNotFoundExceptions;
import com.user.service.external.services.HotelService;
import com.user.service.repositories.UserRepository;
import com.user.service.services.UserService;

@Service
public class UserServiceImpl implements UserService {
	
	@Autowired
	private UserRepository userRepository;
	
	@Autowired
	private RestTemplate restTemplate;
	
	
	// feign client
	@Autowired
	private HotelService hotelService;
	
	private Logger logger=LoggerFactory.getLogger(UserServiceImpl.class);
	
	@Override
	public User saveUser(User user) {

		// generate unique userid
		String randomUserId=UUID.randomUUID().toString();
		user.setUserId(randomUserId);
		return userRepository.save(user);
	}
  // get all users
	
	@Override
	public List<User> getAllUser() {
		
		return userRepository.findAll();
	}
	
	// get single user
	@Override
	public User getUser(String userId) {
		// TODO Auto-generated method stub
		User user= userRepository
				.findById(userId)
				.orElseThrow(()->new ResourceNotFoundExceptions("User with given id is not found on server: "+userId));
		
		// fetch ratings of the above user from ratingService
		//http://localhost:8082/ratings/users/a83383bb-29d7-4330-ab8c-2cbe813a3b24
		
		Rating[] ratingsOfUser=
				restTemplate.getForObject("http://RATINGSERVICES/ratings/users/"+user.getUserId(), Rating[].class );
		
		List<Rating> ratings=Arrays.stream(ratingsOfUser).toList();		
		
		List<Rating> ratingList=ratings.stream().map(rating->{
			// api call to hotel service to get hotel
			// http://localhost:8081/hotels/81acacf2-e760-44b3-9071-1f61796b7f23

			//			ResponseEntity<Hotel> forEntity = restTemplate.getForEntity("http://HOTELSERVICE/hotels/"+rating.getHotelId(), Hotel.class);
			
			// using feign client
			Hotel hotel=hotelService.getHotel(rating.getHotelId());
			
			// set hotel to rating
			
			rating.setHotel(hotel);
			return rating;
			
		}).collect(Collectors.toList());
		
		logger.info("{}",ratingList);
		
		user.setRatings(ratingList);
		
		return user;
		
	}

	@Override
	public void deleteUser(String userId) {
		User user= userRepository
				.findById(userId)
				.orElseThrow(()->new ResourceNotFoundExceptions("User with given id is not found on server: "+userId));;
		userRepository.deleteById(userId);
	}
	
	


}
