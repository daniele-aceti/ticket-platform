package ticket.platform.ticket_platform.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import ticket.platform.ticket_platform.model.User;

public interface UserRepository extends JpaRepository<User, Long> {

    public Optional<User> findByEmail(String username);

    public List<User> findByActive(Boolean active);
}
