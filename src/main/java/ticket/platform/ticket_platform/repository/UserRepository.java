package ticket.platform.ticket_platform.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import ticket.platform.ticket_platform.model.User;

public interface UserRepository extends JpaRepository<User, Long> {

}
