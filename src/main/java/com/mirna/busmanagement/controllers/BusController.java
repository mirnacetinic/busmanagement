package com.mirna.busmanagement.controllers;

import com.mirna.busmanagement.dtos.UserDto;
import com.mirna.busmanagement.mappers.UserMapper;
import com.mirna.busmanagement.models.Bus;
import com.mirna.busmanagement.services.BusService;
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
@RequestMapping("/buses")
public class BusController {

    private final BusService busService;

    @Autowired
    public BusController(BusService busService) {
        this.busService = busService;
    }

    @GetMapping("")
    public String getAllBuses(Model model,
                              @RequestParam(defaultValue = "0") int page,
                              @RequestParam(defaultValue = "8") int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<Bus> buses = busService.getAllBusesPaginated(pageable);
        if (buses.getTotalPages() > 0) {
            List<Integer> pageNumbers = IntStream.rangeClosed(0, buses.getTotalPages() - 1)
                    .boxed()
                    .collect(Collectors.toList());
            model.addAttribute("pageNumbers", pageNumbers);
        }

        model.addAttribute("page", buses);

        return "buses";
    }

    @GetMapping("/add")
    public String showAddBusForm(Model model) {
        model.addAttribute("bus", new Bus());
        return "new_bus";
    }

    @PostMapping("/add")
    public String addBus(@ModelAttribute @Valid Bus bus, BindingResult bindingResult, Model model) {
        if (bindingResult.hasErrors()) {
            return "new_bus";
        }
        try {
            busService.saveBus(bus);
            return "redirect:/buses";
        } catch (Exception e) {
            model.addAttribute("notValid", e.getMessage());
            return "new_bus";
        }
    }

    @GetMapping("/update")
    public String showUpdateBusForm(@RequestParam String busId, Model model) {
        Bus bus = busService.getBusById(busId);
        model.addAttribute("bus", bus);
        model.addAttribute("busId",busId);
        return "update_bus";
    }

    @PutMapping("/update")
    public String updateBus(@RequestParam String busId,@ModelAttribute(name = "bus") @Valid Bus bus,
                             BindingResult bindingResult, Model model) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("busId",busId);
            return "update_bus";
        }
        try {
            busService.updateBus(bus,busId);
            return "redirect:/buses";
        } catch (Exception e) {
            model.addAttribute("notValid", e.getMessage());
            model.addAttribute("busId",busId);
            return "update_bus";
        }
    }

    @DeleteMapping("/delete/{id}")
    public String deleteBus(@PathVariable String id) {
        busService.deleteBus(id);
        return "redirect:/buses";
    }

}
