package com.mirna.busmanagement.controllers;

import com.mirna.busmanagement.models.*;
import com.mirna.busmanagement.security.CustomUserDetails;
import com.mirna.busmanagement.services.*;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;


@Controller
@RequestMapping("/routes")
public class RouteController {

    private final RouteService routeService;
    private final BusService busService;
    private final LocationService locationService;
    private final UserService userService;
    private final TicketService ticketService;

    @Autowired
    public RouteController(RouteService routeService, BusService busService, LocationService locationService, UserService userService, TicketService ticketService) {
        this.routeService = routeService;
        this.busService = busService;
        this.locationService = locationService;
        this.userService = userService;
        this.ticketService = ticketService;
    }

    @GetMapping("")
    public String getAllRoutes(@AuthenticationPrincipal CustomUserDetails customUserDetails,
                               Model model,
                               @RequestParam(defaultValue = "0") int page,
                               @RequestParam(defaultValue = "8") int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("departureDate").descending());
        Page<Route> routePage = ((customUserDetails.getAuthorities().toString().contains("ROLE_ADMIN")) ? routeService.getAllRoutesPaginated(pageable) : routeService.getAllDriverRoutesPaginated(pageable, customUserDetails.getUserId()));
        model.addAttribute("page", routePage);
        if (routePage.getTotalPages() > 0) {
            List<Integer> pageNumbers = IntStream.rangeClosed(0, routePage.getTotalPages() - 1)
                    .boxed()
                    .collect(Collectors.toList());
            model.addAttribute("pageNumbers", pageNumbers);
        }

        return "routes";
    }

    @PostMapping("/search")
    public String searchRoutes(
            @RequestParam(name = "destination", required = false, defaultValue = "") String destination,
            @RequestParam(name = "origin", required = false, defaultValue = "") String origin,
            @RequestParam(name = "departureDate", required = false, defaultValue = "") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate departureDate,
            Model model) {

        try {
            List<Route> searchResults = routeService.searchRoutes(destination, origin, departureDate);
            searchResults.removeIf(route -> !ticketService.availableTickets(route));
            model.addAttribute("searchResults", searchResults);

            return "search_results";

        } catch (Exception e) {
            model.addAttribute("notValid", e.getMessage());
            return "search_results";
        }
    }

    @GetMapping("/add")
    public String showAddRouteForm(Model model) {
        model.addAttribute("route", new Route());
        model.addAttribute("locations", locationService.getAllLocations());
        model.addAttribute("drivers", userService.getUsersByRole(Role.ROLE_DRIVER));
        model.addAttribute("buses", busService.getAllBuses());

        return "new_route";
    }

    @PostMapping("/add")
    public String addRoute(@Valid @ModelAttribute Route route,
                           BindingResult bindingResult, Model model) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("locations", locationService.getAllLocations());
            model.addAttribute("drivers",  userService.getUsersByRole(Role.ROLE_DRIVER));
            model.addAttribute("buses", busService.getAllBuses());
            return "new_route";
        }
        try {
            routeService.saveRoute(route);
            return "redirect:/routes";
        }
        catch (Exception e){
            model.addAttribute("notValid",e.getMessage());
            model.addAttribute("locations", locationService.getAllLocations());
            model.addAttribute("drivers",  userService.getUsersByRole(Role.ROLE_DRIVER));
            model.addAttribute("buses", busService.getAllBuses());
            return "new_route";
        }


    }

    @GetMapping("/update")
    public String showUpdateRouteForm(@RequestParam(name = "routeId") String id, Model model) {
        model.addAttribute("route", routeService.getRouteById(id));
        model.addAttribute("routeId", id);
        model.addAttribute("buses",busService.getAllBuses());
        model.addAttribute("users",userService.getAllUsers());
        model.addAttribute("drivers", userService.getUsersByRole(Role.ROLE_DRIVER));
        model.addAttribute("locations", locationService.getAllLocations());


        return "update_route";
    }

    @PutMapping("/update")
    public String updateTicket(@RequestParam(name = "routeId") String id, @ModelAttribute(name = "route") @Valid Route route,
                               BindingResult bindingResult, Model model) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("routeId", id);
            model.addAttribute("buses",busService.getAllBuses());
            model.addAttribute("users",userService.getAllUsers());
            model.addAttribute("drivers", userService.getUsersByRole(Role.ROLE_DRIVER));
            model.addAttribute("locations", locationService.getAllLocations());
            return "update_route";
        }
        try {
            routeService.updateRoute(route, id);
            return "redirect:/routes";

        } catch (Exception e) {
            model.addAttribute("notValid", e.getMessage());
            model.addAttribute("routeId", id);
            model.addAttribute("buses",busService.getAllBuses());
            model.addAttribute("users",userService.getAllUsers());
            model.addAttribute("drivers", userService.getUsersByRole(Role.ROLE_DRIVER));
            model.addAttribute("locations", locationService.getAllLocations());

            return "update_route";
        }
    }

    @DeleteMapping("/delete/{id}")
    public String deleteRoute(@PathVariable String id) {
        routeService.deleteRoute(id);
        return "redirect:/routes";
    }

}
