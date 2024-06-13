package com.mirna.busmanagement.repositories;

import com.mirna.busmanagement.models.Bus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface BusRepository extends JpaRepository<Bus, String> {
    Optional<Bus> findByRegistration(String bus);
}
