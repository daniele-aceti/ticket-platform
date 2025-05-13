package ticket.platform.ticket_platform.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import ticket.platform.ticket_platform.model.Notes;

public interface NotesRepository extends JpaRepository<Notes, Long> {

}
