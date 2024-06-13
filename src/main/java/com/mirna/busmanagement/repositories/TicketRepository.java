package com.mirna.busmanagement.repositories;

import com.mirna.busmanagement.models.Ticket;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TicketRepository extends JpaRepository<Ticket, String> {
    List<Ticket> getTicketsByRoute_Id(String route_id);

    Page<Ticket> getTicketsByUserId(String user_id, Pageable pageable);
}
