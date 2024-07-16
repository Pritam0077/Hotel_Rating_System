package com.user.service.controllers;

import java.util.List;

import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.user.service.entities.User;
import com.user.service.services.UserService;

import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.ratelimiter.annotation.RateLimiter;
import lombok.AllArgsConstructor;

@CrossOrigin("*")
@AllArgsConstructor
@RestController
@RequestMapping("/users")
public class UserController {
	
	@Autowired
	private UserService userService;
	
	
	//create
	@PostMapping
	public ResponseEntity<User> createUser(@RequestBody User user){
		
		User user1=userService.saveUser(user);
		return ResponseEntity.status(HttpStatus.CREATED).body(user1);
	}
	
	
	//single user get
	@GetMapping("{userId}")
	@CircuitBreaker(name="ratingHotelBreaker",fallbackMethod="ratingHotelFallback")
//	@RateLimiter(name="userRateLimiter",fallbackMethod = "ratingHotelFallback")
	
	public ResponseEntity<User> getSingleUser(@PathVariable String userId){
		User user=userService.getUser(userId);
		return ResponseEntity.ok(user);
	}
	
	// creating fallback for circuit breaker-It executes only when the service gets failed
	
	public ResponseEntity<User> ratingHotelFallback(String userId,Exception ex){
		User user=User.builder()
				.email("dummy@gmail.com")
				.name("Dummy")
				.about("User created dummy because service is down")
				.userId("1234")
				.build();
		return new ResponseEntity<>(user,HttpStatus.OK);
	}
	
//	@GetMapping("/test")
//	public ResponseEntity<String> testEndpoint() {
//	    return ResponseEntity.ok("Test endpoint works");
//	}

	//all user get
	@GetMapping
	public ResponseEntity<List<User>> getAllUser(){
		List<User> allUser=userService.getAllUser();
		return ResponseEntity.ok(allUser);
	}
	
	// delete user by Id
	@DeleteMapping("{userId}")
	public ResponseEntity<String> deleteUser(@PathVariable String userId){
		userService.deleteUser(userId);
		return ResponseEntity.ok("User deleted succesfully");
	}
}
