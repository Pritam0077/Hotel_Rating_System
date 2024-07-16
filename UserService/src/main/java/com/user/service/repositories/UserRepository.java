package com.user.service.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.user.service.entities.User;

// contains db operations
public interface UserRepository extends JpaRepository<User, String>{

	// write any custom methods
	
}
