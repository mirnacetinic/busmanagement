package com.mirna.busmanagement.services;

import com.mirna.busmanagement.models.Bus;
import com.mirna.busmanagement.models.Location;
import com.mirna.busmanagement.repositories.LocationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class LocationService {
    private final LocationRepository locationRepository;

    @Autowired
    public LocationService(LocationRepository locationRepository) {
        this.locationRepository = locationRepository;
    }

    public List<Location> getAllLocations() {
        return locationRepository.findAll();
    }

    public Page<Location> getAllLocationsPaginated(Pageable pageable) {
        return locationRepository.findAll(pageable);
    }

    public Location getLocationById(String id) {
        return locationRepository.findById(id).orElse(null);
    }

    public void saveLocation(Location location) {
        try {
            locationRepository.save(location);
        } catch (Exception e) {
            throw new RuntimeException("Failed to save, location ZIP code must be unique!");
        }
    }

    public void deleteLocation(String id) {
        locationRepository.deleteById(id);
    }

    public void updateLocation(Location location, String id) {
       Location existingLocation=getLocationById(id);
        if(!location.getZIP().equals(existingLocation.getZIP())){
            if(!uniqueZIP(location.getZIP())){
                throw new RuntimeException("City with this ZIP code already exists!");
            }
        }

        try {
            existingLocation.setCity(location.getCity());
            existingLocation.setCountry(location.getCountry());
            existingLocation.setZIP(location.getZIP());
            locationRepository.save(existingLocation);

        } catch (Exception e) {
            throw new RuntimeException("Error updating the bus.", e);
        }
    }

    private boolean uniqueZIP(String zip) {
        return locationRepository.findByZIP(zip).isEmpty();
    }
}
