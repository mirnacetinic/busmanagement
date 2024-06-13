package com.mirna.busmanagement.controllers;

import com.mirna.busmanagement.models.Route;
import com.mirna.busmanagement.models.Ticket;
import com.mirna.busmanagement.models.User;
import com.mirna.busmanagement.security.CustomUserDetails;
import com.mirna.busmanagement.services.RouteService;
import com.mirna.busmanagement.services.TicketService;
import com.mirna.busmanagement.services.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Controller
@RequestMapping("/ticket")
public class TicketController {

    private final TicketService ticketService;
    private final UserService userService;
    private final RouteService routeService;


    @Autowired
    public TicketController(TicketService ticketService, UserService userService, RouteService routeService) {
        this.ticketService = ticketService;
        this.userService = userService;
        this.routeService = routeService;
    }

    @GetMapping("")
    public String getAllTickets(@AuthenticationPrincipal CustomUserDetails customUserDetails,
                                Model model,
                                @RequestParam(defaultValue = "0") int page,
                                @RequestParam(defaultValue = "8") int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("route.DepartureDate").descending());
        Page<Ticket> ticketPage = ((customUserDetails.getAuthorities().toString().contains("ROLE_ADMIN")) ?
                ticketService.getAllTicketsPaginated(pageable) : ticketService.getAllUserTicketsPaginated(customUserDetails.getUserId(), pageable));
        if (ticketPage.getTotalPages() > 0) {
            List<Integer> pageNumbers = IntStream.rangeClosed(0, ticketPage.getTotalPages() - 1)
                    .boxed()
                    .collect(Collectors.toList());
            model.addAttribute("pageNumbers", pageNumbers);
        }

        model.addAttribute("page", ticketPage);
        return "tickets";
    }

    @PostMapping("/buy")
    public String buyTicket(Model model, @AuthenticationPrincipal CustomUserDetails customUserDetails, @RequestParam(name = "route_id") String route_id, @RequestParam(name = "passengers") String passengers) {
        try {
            ticketService.saveTicket(customUserDetails.getUserId(), route_id, passengers);
            return "redirect:/ticket";
        } catch (Exception e) {
            model.addAttribute("notValid", e.getMessage());
            return "search_results";
        }

    }

    @GetMapping("/add")
    public String showAddTicketForm(Model model) {
        model.addAttribute("ticket", new Ticket());
        ticketService.repopulateModel(model);
        return "new_ticket";
    }

    @PostMapping("/add")
    public String addTicket(@ModelAttribute Ticket ticket,Model model) {
        try {
            ticketService.addTicket(ticket);
            return "redirect:/ticket";
        }
        catch (Exception e){
            model.addAttribute("notValid",e.getMessage());
            ticketService.repopulateModel(model);
            return "new_ticket";
        }


    }

    @GetMapping("/update")
    public String showUpdateTicketForm(@RequestParam(name = "ticketId") String id, Model model) {
        Ticket ticket = ticketService.getTicketById(id);
        model.addAttribute("ticket", ticket);
        model.addAttribute("ticketId", id);
        List<User> users = userService.getAllUsers();
        List<Route> routes = routeService.getAllRoutes();
        model.addAttribute("users", users);
        model.addAttribute("routes", routes);

        return "update_ticket";
    }

    @PutMapping("/update")
    public String updateTicket(@RequestParam(name = "ticketId") String id, @ModelAttribute(name = "ticket") @Valid Ticket ticket,
                               BindingResult bindingResult, Model model) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("ticketId", id);
            List<User> users = userService.getAllUsers();
            List<Route> routes = routeService.getAllRoutes();
            model.addAttribute("users", users);
            model.addAttribute("routes", routes);
            return "update_ticket";
        }
        try {
            ticketService.updateTicket(ticket, id);
            return "redirect:/ticket";
        } catch (Exception e) {
            model.addAttribute("notValid", e.getMessage());
            model.addAttribute("ticketId", id);
            List<User> users = userService.getAllUsers();
            List<Route> routes = routeService.getAllRoutes();
            model.addAttribute("users", users);
            model.addAttribute("routes", routes);
            return "update_ticket";
        }
    }

    @DeleteMapping("/delete/{id}")
    public String deleteTicket(@PathVariable String id) {
        ticketService.deleteTicket(id);
        return "redirect:/ticket";
    }


}
