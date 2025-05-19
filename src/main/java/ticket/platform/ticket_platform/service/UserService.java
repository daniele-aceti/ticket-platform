package ticket.platform.ticket_platform.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import ticket.platform.ticket_platform.model.Role;
import ticket.platform.ticket_platform.model.User;
import ticket.platform.ticket_platform.repository.RoleRepository;
import ticket.platform.ticket_platform.repository.TicketRepository;
import ticket.platform.ticket_platform.repository.UserRepository;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;

    @Autowired
    public UserService(UserRepository userRepository, TicketRepository ticketRepository,
            RoleRepository roleRepository) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
    }

    public List<User> findUserActive(boolean active) {
        List<User> result = new ArrayList<>();
        result = userRepository.findByActive(active);
        return result;
    }

    public List<User> findAllUser() {
        return userRepository.findAll();
    }

    public Optional<User> findLogUser(String username) {
        return userRepository.findByEmail(username);
    }

    public void operatoreCreate(User formNewUser) {
        Role operatore = roleRepository.findByRoleName("OPERATORE");
        formNewUser.setRole(List.of(operatore));//mi da una nuova lista con solo un elemento OPERATORE
        formNewUser.setPassword("{noop}" + formNewUser.getPassword());
        userRepository.save(formNewUser);
    }

}
