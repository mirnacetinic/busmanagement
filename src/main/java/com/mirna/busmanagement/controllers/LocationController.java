package com.mirna.busmanagement.controllers;
import com.mirna.busmanagement.models.Location;
import com.mirna.busmanagement.services.LocationService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Controller
@RequestMapping("/location")
public class LocationController {

    private final LocationService locationService;

    @Autowired
    public LocationController(LocationService locationService) {
        this.locationService = locationService;

    }

    @GetMapping("")
    public String getAllLocationsPaginated(Model model,
                                           @RequestParam(defaultValue = "0") int page,
                                           @RequestParam(defaultValue = "8") int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<Location> locations = locationService.getAllLocationsPaginated(pageable);

        model.addAttribute("page", locations);
        if (locations.getTotalPages() > 0) {
            List<Integer> pageNumbers = IntStream.rangeClosed(0, locations.getTotalPages() - 1)
                    .boxed()
                    .collect(Collectors.toList());
            model.addAttribute("pageNumbers", pageNumbers);
        }

        return "locations";
    }

    @GetMapping("/add")
    public String showAddLocationForm(Model model) {
        model.addAttribute("location", new Location());
        return "new_location";
    }

    @PostMapping("/add")
    public String addLocation(@ModelAttribute @Valid Location location, BindingResult bindingResult, Model model) {
        if (bindingResult.hasErrors()) {
            return "new_location";
        }

        try {
            locationService.saveLocation(location);
            return "redirect:/location";
        } catch (Exception e) {
            model.addAttribute("notValid", e.getMessage());
            return "new_location";
        }
    }

    @GetMapping("/update")
    public String showUpdateLocationForm(@RequestParam(name = "locationId") String id, Model model) {
        Location location = locationService.getLocationById(id);
        model.addAttribute("location", location);
        model.addAttribute("locationId",id);
        return "update_location";
    }

    @PutMapping("/update")
    public String updateLocation(@RequestParam(name = "locationId") String id,@ModelAttribute(name = "location") @Valid Location location,
                            BindingResult bindingResult, Model model) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("locationId",id);
            return "update_location";
        }
        try {
            locationService.updateLocation(location,id);
            return "redirect:/location";
        } catch (Exception e) {
            model.addAttribute("notValid", e.getMessage());
            model.addAttribute("locationId",id);
            return "update_location";
        }
    }

    @DeleteMapping("/delete/{id}")
    public String deleteLocation(@PathVariable String id) {
        locationService.deleteLocation(id);
        return "redirect:/location";
    }


}
