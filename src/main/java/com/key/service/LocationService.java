package com.key.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.key.model.Location;
@Service
public interface LocationService {

	public List<Location> getAllLocation();
	public Location findById(Integer id);
}
