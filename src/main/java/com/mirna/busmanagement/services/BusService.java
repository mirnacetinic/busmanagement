package com.mirna.busmanagement.services;
import com.mirna.busmanagement.models.Bus;
import com.mirna.busmanagement.repositories.BusRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BusService {
    private final BusRepository busRepository;

    @Autowired
    public BusService(BusRepository busRepository) {
        this.busRepository = busRepository;
    }

    public List<Bus> getAllBuses() {
        return busRepository.findAll();
    }

    public Page<Bus> getAllBusesPaginated(Pageable pageable) {
        return busRepository.findAll(pageable);
    }

    public Bus getBusById(String id) {
        return busRepository.findById(id).orElse(null);
    }

    public boolean registrationTaken(String registration){
        return busRepository.findByRegistration(registration).isPresent();
    }
    public void saveBus(Bus bus) {
        try {
            busRepository.save(bus);
        } catch (Exception e) {
            throw new RuntimeException("Failed to save, bus registration must be unique!");
        }
    }

    public void deleteBus(String id) {
        busRepository.deleteById(id);
    }

    public void updateBus(Bus bus, String busId) {
        Bus existingBus=getBusById(busId);
        if(!bus.getRegistration().equals(existingBus.getRegistration())){
            if(registrationTaken(bus.getRegistration())){
                throw new RuntimeException("Bus with this registration already exists!");
            }
        }

            try {
                existingBus.setCapacity(bus.getCapacity());
                existingBus.setRegistration(bus.getRegistration());
                busRepository.save(existingBus);

            } catch (Exception e) {
                throw new RuntimeException("Error updating the bus.", e);
            }
    }
}
