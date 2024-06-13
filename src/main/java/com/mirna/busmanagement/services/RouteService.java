package com.mirna.busmanagement.services;

import com.mirna.busmanagement.models.Route;
import com.mirna.busmanagement.repositories.RouteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public class RouteService {

    private final RouteRepository routeRepository;

    @Autowired
    public RouteService(RouteRepository routeRepository) {
        this.routeRepository = routeRepository;
    }

    public Page<Route> getAllRoutesPaginated(Pageable pageable) {
        return routeRepository.findAll(pageable);
    }

    public Page<Route> getAllDriverRoutesPaginated(Pageable pageable, String driver_id) {
        return routeRepository.findByDriver_IdAndDepartureDateGreaterThanEqual(driver_id, pageable, LocalDate.now());
    }

    public List<Route> getAllRoutes() {
        return routeRepository.findAll();
    }

    public int getRouteCapacity(String id){
        return getRouteById(id).getBus().getCapacity();
    }

    public List<Route> searchRoutes(String destination, String origin, LocalDate departureDate) {
        if (destination.isBlank() && origin.isBlank() && departureDate == null) {
            return routeRepository.findByDepartureDateGreaterThanEqual(LocalDate.now());
        }

        if (!destination.isBlank() && !origin.isBlank() && departureDate != null) {
            return routeRepository.findByDestinationCityIgnoreCaseAndOriginCityIgnoreCaseAndDepartureDate(destination, origin, departureDate);
        } else if (!destination.isBlank() && !origin.isBlank()) {
            return routeRepository.findByDestinationCityIgnoreCaseAndOriginCityIgnoreCaseAndDepartureDateGreaterThanEqual(destination, origin, LocalDate.now());
        } else if (departureDate != null) {
            if (departureDate.isBefore(LocalDate.now())) {
                throw new RuntimeException("Can't search for past dates!");
            } else {
                return routeRepository.findByDepartureDate(departureDate);
            }
        } else if (!destination.isBlank()) {
            return routeRepository.findByDestinationCityIgnoreCaseAndDepartureDateGreaterThanEqual(destination, LocalDate.now());
        } else {
            return routeRepository.findByOriginCityIgnoreCaseAndDepartureDateGreaterThanEqual(origin, LocalDate.now());
        }
    }

    public Route getRouteById(String id) {
        return routeRepository.findById(id).orElse(null);
    }

    public void saveRoute(Route route) {
        if(route.getDestination()==route.getOrigin()){
            throw new RuntimeException("Origin and destination can't be same!!");
        } else if (route.getDuration().toString().equals("00:00")) {
            throw new RuntimeException("Duration minimum is 1 minute!");

        }

        routeRepository.save(route);
    }

    public void deleteRoute(String id) {
        routeRepository.deleteById(id);
    }

    public void updateRoute(Route route, String id) {
        Route existingRoute=routeRepository.getRouteById(id);
        if(route.getDestination()==route.getOrigin()){
            throw new RuntimeException("Origin and destination can't be same!!");
        } else if (route.getDuration().toString().equals("00:00")) {
            throw new RuntimeException("Duration minimum is 1 minute!");

        }

        existingRoute.setDriver(route.getDriver());
        existingRoute.setDestination(route.getDestination());
        existingRoute.setOrigin(route.getOrigin());
        existingRoute.setBus(route.getBus());
        existingRoute.setDepartureDate(route.getDepartureDate());
        existingRoute.setDepartureTime(route.getDepartureTime());
        existingRoute.setCost(route.getCost());
        existingRoute.setDuration(route.getDuration());

        routeRepository.save(existingRoute);

    }
}
