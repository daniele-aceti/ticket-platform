package ticket.platform.ticket_platform.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import ticket.platform.ticket_platform.model.Ticket;

public interface TicketRepository extends JpaRepository<Ticket, Long> {

    public List<Ticket> findByTitleContainingIgnoreCase(String title);

}
