package ticket.platform.ticket_platform.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import ticket.platform.ticket_platform.model.Notes;
import ticket.platform.ticket_platform.model.Ticket;
import ticket.platform.ticket_platform.model.User;
import ticket.platform.ticket_platform.repository.CategoryRepository;
import ticket.platform.ticket_platform.repository.NotesRepository;
import ticket.platform.ticket_platform.repository.TicketRepository;
import ticket.platform.ticket_platform.repository.UserRepository;

@Service
public class TicketService {

    private final NotesRepository notesRepository;
    private final TicketRepository ticketRepository;
    private final CategoryRepository categoryRepository;
    private final UserRepository userRepository;

    @Autowired
    public TicketService(TicketRepository ticketRepository, CategoryRepository categoryRepository,
            UserRepository userRepository, NotesRepository notesRepository) {
        this.ticketRepository = ticketRepository;
        this.categoryRepository = categoryRepository;
        this.userRepository = userRepository;
        this.notesRepository = notesRepository;
    }

    public List<Ticket> findTicketList(String title, Authentication authentication) {
        List<Ticket> result = new ArrayList<>();
        Boolean isAdmin = false;
        String username = authentication.getName();
        User user = userRepository.findByEmail(username).get();
        Long userId = user.getId();
        for (GrantedAuthority authority : authentication.getAuthorities()) {
            if (authority.getAuthority().equals("ADMIN")) {
                isAdmin = true;
                break;
            }
        }
        if (title != null && !title.isBlank()) {
            result = ticketRepository.findByTitleContainingIgnoreCase(title);
        } else if (isAdmin) {
            //tutti i ticket quando è admin
            result = ticketRepository.findAll();
        } else {
            //prende solo i ticket di quell'user
            result = ticketRepository.findByUserId(userId);
        }
        return result;
    }

    public Ticket create(Ticket ticket) {
        return ticketRepository.save(ticket);
    }

    public Optional<Ticket> findByIdTicket(Long id) {
        return ticketRepository.findById(id);
    }

    public List<Ticket> findAllTicket() {
        return ticketRepository.findAll();
    }

    public List<Ticket> findStatus(String value) {
        return ticketRepository.findByStatusContainingIgnoreCase(value);
    }

    public List<Ticket> findCategory(String value) {
        return ticketRepository.findByCategoriesCategoryNameContainingIgnoreCase(value);
    }

    public List<Ticket> findTitle(String value) {
        return ticketRepository.findByTitleContainingIgnoreCase(value);
    }

    public void deleteTicket(Long id) {
        Ticket ticket = ticketRepository.findById(id).get();
        for (Notes notes : ticket.getNotes()) {
            notesRepository.delete(notes);
        }
        ticketRepository.deleteById(id);
    }

    public void changeStatusTicket(String newStatus, Long ticketId, RedirectAttributes redirectAttributes) {
        Ticket ticket = findByIdTicket(ticketId).get();
        ticket.setStatus(newStatus);
        create(ticket);
        redirectAttributes.addFlashAttribute("messageChangeStatus", "Il ticket numero: "
                + ticket.getId() + " " + ticket.getTitle().toLowerCase() + " è stato aggiornato a: "
                + newStatus.toLowerCase());
    }

    public boolean ticketDetails(Long id, Model model) {
        boolean flag = false;
        Optional<Ticket> optTicket = findByIdTicket(id);
        if (optTicket.isPresent()) {
            model.addAttribute("detailsTicket", optTicket.get());
            /* cerca tutte le notes associate ad un determinato ticket */
            model.addAttribute("notesList", notesRepository.findByTicket(optTicket.get()));
            flag = true;
        }
        return flag;
    }
}
