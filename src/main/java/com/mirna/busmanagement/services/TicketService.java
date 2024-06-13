package com.mirna.busmanagement.services;

import com.mirna.busmanagement.models.Bus;
import com.mirna.busmanagement.models.Route;
import com.mirna.busmanagement.models.Ticket;
import com.mirna.busmanagement.repositories.TicketRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;

import java.util.Collection;
import java.util.List;

@Service
public class TicketService {

    private final TicketRepository ticketRepository;
    private final UserService userService;
    private final RouteService routeService;

    @Autowired
    public TicketService(TicketRepository ticketRepository, UserService userService, RouteService routeService) {
        this.ticketRepository = ticketRepository;
        this.userService = userService;
        this.routeService = routeService;
    }

    public Page<Ticket> getAllTicketsPaginated(Pageable pageable) {
        return ticketRepository.findAll(pageable);
    }

    public Page<Ticket> getAllUserTicketsPaginated(String user_id, Pageable pageable) {
        return ticketRepository.getTicketsByUserId(user_id, pageable);
    }

    public void saveTicket(String userId, String routeId, String passengers) {
        Ticket ticket = new Ticket();
        if (Integer.parseInt(passengers) > seatsLeft(routeId)) {
            throw new RuntimeException("Sorry, we don't have this many seats left");
        } else {
            ticket.setUser(userService.getUserById(userId));
            ticket.setRoute(routeService.getRouteById(routeId));
            ticket.setPassengers(Integer.parseInt(passengers));
            ticketRepository.save(ticket);
        }
    }

    public void repopulateModel(Model model){
        model.addAttribute("users",userService.getAllUsers());
        List<Route> routes = routeService.getAllRoutes();
        routes.removeIf(route -> !availableTickets(route));
        model.addAttribute("routes",routes);
    }
    public List<Ticket> getTicketsForRoute(String routeId) {
        return ticketRepository.getTicketsByRoute_Id(routeId);
    }

    public int takenSeats(String routeId) {
        List<Ticket> tickets = getTicketsForRoute(routeId);
        return tickets.stream()
                .mapToInt(Ticket::getPassengers)
                .sum();
    }

    public int seatsLeft(String routeId) {
        return routeService.getRouteCapacity(routeId)- takenSeats(routeId);
    }

    public boolean availableTickets(Route route) {
        return route.getBus().getCapacity() > takenSeats(route.getId());
    }

    public Ticket getTicketById(String id) {
        return ticketRepository.findById(id).orElse(null);
    }

    public void deleteTicket(String id) {
        ticketRepository.deleteById(id);
    }

    public void addTicket(Ticket ticket) {
        if (ticket.getPassengers()>seatsLeft(ticket.getRoute().getId())) {
            throw new RuntimeException("Exceeded capacity");
        } else {
            ticketRepository.save(ticket);
        }
    }

    public void updateTicket(Ticket ticket, String id) {
        Ticket existingTicket=getTicketById(id);
        if(existingTicket.getRoute()==ticket.getRoute()){
            if(seatsLeft(existingTicket.getRoute().getId())<ticket.getPassengers()-existingTicket.getPassengers()){
                throw new RuntimeException("Exceeded capacity");
            }
            existingTicket.setUser(ticket.getUser());
            existingTicket.setPassengers(ticket.getPassengers());

            }

        else if(seatsLeft(ticket.getRoute().getId())<ticket.getPassengers()){
            throw new RuntimeException("Exceeded capacity");

        }

        else{
            existingTicket.setRoute(ticket.getRoute());
            existingTicket.setPassengers(ticket.getPassengers());
            existingTicket.setUser(ticket.getUser());
        }

        ticketRepository.save(existingTicket);



    }
}
