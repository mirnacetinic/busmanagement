package com.mirna.busmanagement.repositories;

import com.mirna.busmanagement.models.Location;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface LocationRepository extends JpaRepository<Location, String> {
    Optional<Location> findByZIP(String ZIP);
}
