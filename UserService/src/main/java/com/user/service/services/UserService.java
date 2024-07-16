package com.user.service.services;

import java.util.List;

import com.user.service.entities.User;

public interface UserService {
	
	// create
	User saveUser(User user);
	
	//get all user
	List<User> getAllUser();
	
	//get single user of given Id of user
	User getUser(String userId);
	
	void deleteUser(String userId);
	
	// TODO: delete
	// 
}
