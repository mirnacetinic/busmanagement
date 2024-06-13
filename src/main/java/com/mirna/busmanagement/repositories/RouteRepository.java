package com.mirna.busmanagement.repositories;

import com.mirna.busmanagement.models.Route;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface RouteRepository extends JpaRepository<Route, String> {

    List<Route> findByDestinationCityIgnoreCaseAndOriginCityIgnoreCaseAndDepartureDate(String destination, String origin, LocalDate departureDate);

    List<Route> findByDestinationCityIgnoreCaseAndOriginCityIgnoreCaseAndDepartureDateGreaterThanEqual(String destination, String origin, LocalDate today);

    List<Route> findByDepartureDate(LocalDate departureDate);

    List<Route> findByDepartureDateGreaterThanEqual(LocalDate today);

    List<Route> findByDestinationCityIgnoreCaseAndDepartureDateGreaterThanEqual(String destination, LocalDate today);

    List<Route> findByOriginCityIgnoreCaseAndDepartureDateGreaterThanEqual(String origin, LocalDate today);

    Page<Route> findByDriver_IdAndDepartureDateGreaterThanEqual(String driver_id, Pageable pageable, LocalDate today);

    Route getRouteById(String id);
}
