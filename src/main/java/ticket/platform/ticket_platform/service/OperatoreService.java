package ticket.platform.ticket_platform.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import ticket.platform.ticket_platform.model.Ticket;
import ticket.platform.ticket_platform.model.User;
import ticket.platform.ticket_platform.repository.TicketRepository;
import ticket.platform.ticket_platform.repository.UserRepository;

@Service
public class OperatoreService {

    private final UserRepository userRepository;
    private final TicketRepository ticketRepository;

    public OperatoreService(UserRepository userRepository,TicketRepository ticketRepository) {
        this.userRepository = userRepository;
        this.ticketRepository = ticketRepository;
    }

    public boolean userEdit(Long id, User newDataUser, BindingResult bindingResult, Model model,
            RedirectAttributes redirectAttribute) {
        boolean flag = false;
        User user = userRepository.findById(id).get();
        if (user == null) {
            flag = true;
            redirectAttribute.addFlashAttribute("ErrorUser", "Utente non trovato");
        }

        boolean changed = false;

        // Confronta nome
        if (!user.getName().equals(newDataUser.getName())) {
            user.setName(newDataUser.getName());
            changed = true;
        }

        // Confronta email
        if (!user.getEmail().equals(newDataUser.getEmail())) {
            user.setEmail(newDataUser.getEmail());
            changed = true;
        }

        // Controlla ticket status e active
        List<Ticket> userTickets = ticketRepository.findByUserId(user.getId());
        //se la lista è vuota conseti attivazione/disattivazione
        if (userTickets.isEmpty()) {
            if (user.isActive() != newDataUser.isActive()) {
                user.setActive(newDataUser.isActive());
                changed = true;
            }
        } else {
            boolean allCompleted = true;

            //se è piena ma anche un solo ticket non è completato blocca!
            for (Ticket ticket : userTickets) {
                if (!ticket.getStatus().equalsIgnoreCase("Completato")) {
                    allCompleted = false;
                    break;
                }
            }

            //se tutti sono completati consenti la disattivazione
            if (allCompleted) {
                if (user.isActive() != newDataUser.isActive()) {
                    user.setActive(newDataUser.isActive());
                    changed = true;
                }
            } else {
                redirectAttribute.addFlashAttribute("ErrorActive",
                        "Non puoi scollegarti: ci sono ancora ticket da chiudere.");
                flag = true;

            }

        }

        // Password (solo se modificata e non vuota)
        if (newDataUser.getPassword() != null && !newDataUser.getPassword().isBlank()) {
            //imposta password criptata
            user.setPassword("{noop}" + newDataUser.getPassword());
            changed = true;
        }

        if (changed) {
            userRepository.save(user);
            redirectAttribute.addFlashAttribute("messageEditUser",
                    "I tuoi dati sono stati aggiornati");
        }
        return flag;
    }

}
