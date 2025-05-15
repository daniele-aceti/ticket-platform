package ticket.platform.ticket_platform.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import ticket.platform.ticket_platform.model.Notes;
import ticket.platform.ticket_platform.model.Ticket;

public interface NotesRepository extends JpaRepository<Notes, Long> {
    List<Notes> findByTicket(Ticket ticket);
}
