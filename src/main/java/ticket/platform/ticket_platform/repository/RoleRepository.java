package ticket.platform.ticket_platform.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import ticket.platform.ticket_platform.model.Role;

public interface RoleRepository extends JpaRepository<Role, Long> {
    Role findByRoleName(String roleName);
}
