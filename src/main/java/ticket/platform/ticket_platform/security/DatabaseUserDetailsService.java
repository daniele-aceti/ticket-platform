package ticket.platform.ticket_platform.security;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import ticket.platform.ticket_platform.model.User;
import ticket.platform.ticket_platform.repository.UserRepository;

@Service
public class DatabaseUserDetailsService implements UserDetailsService {

    @Autowired
    private UserRepository userRepository;

    public DatabaseUserDetailsService() {
    }

    public DatabaseUserDetailsService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<User> optUser = userRepository.findByEmail(username);

        if (optUser.isPresent()) {
            return new DatabaseUserDetails(optUser.get());
        } else {
            throw new UsernameNotFoundException("Username not found");
        }
    }

}
