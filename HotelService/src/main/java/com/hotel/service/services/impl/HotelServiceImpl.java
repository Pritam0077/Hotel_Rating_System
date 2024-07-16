package com.hotel.service.services.impl;

import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.hotel.service.entities.Hotel;
import com.hotel.service.exception.ResourceNotFoundException;
import com.hotel.service.repositories.HotelRepo;
import com.hotel.service.services.HotelService;


@Service
public class HotelServiceImpl implements HotelService {
	
	@Autowired
	private HotelRepo hotelRepository;
	
	@Override
	public Hotel create(Hotel hotel) {
		String hotelId=UUID.randomUUID().toString();
		hotel.setId(hotelId);
		System.out.println("Generated Hotel ID: " + hotelId); // Debugging line
		return hotelRepository.save(hotel);
	}

	@Override
	public List<Hotel> getAll() {
		// TODO Auto-generated method stub
		return hotelRepository.findAll();
	}

	@Override
	public Hotel get(String id) {
		return hotelRepository
				.findById(id)
				.orElseThrow(()->new ResourceNotFoundException("Hotel with given id not found"));
	}

	@Override
	public void delete(String id) {
		// TODO Auto-generated method stub
		Hotel hotel=hotelRepository.findById(id)
				.orElseThrow(()->new ResourceNotFoundException("Hotel with given id not found"));
		
		hotelRepository.delete(hotel);
		
	}

}
