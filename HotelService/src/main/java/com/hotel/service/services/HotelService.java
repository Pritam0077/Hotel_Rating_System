package com.hotel.service.services;

import java.util.List;

import com.hotel.service.entities.Hotel;

public interface HotelService {
	
	//create
	Hotel create(Hotel hotel);
	
	//getall
	List<Hotel> getAll();
	
	//getsingle
	Hotel get(String id);
	
	//deletesingle
	void delete(String id);
	}
