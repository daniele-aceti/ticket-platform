package ticket.platform.ticket_platform.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import ticket.platform.ticket_platform.model.Notes;
import ticket.platform.ticket_platform.model.Ticket;
import ticket.platform.ticket_platform.model.User;
import ticket.platform.ticket_platform.repository.NotesRepository;

@Service
public class NotesService {

    private final UserService userService;

    private final TicketService ticketService;

    private final NotesRepository notesRepository;

    @Autowired
    public NotesService(NotesRepository notesRepository, TicketService ticketService, UserService userService) {
        this.notesRepository = notesRepository;
        this.ticketService = ticketService;
        this.userService = userService;
    }

    public List<Notes> findByNotes(Ticket ticket) {
        return notesRepository.findByTicket(ticket);
    }

    public void deleteNotes(Long id) {
        notesRepository.deleteById(id);
    }

    public Notes viewNotes(Long idTicket, Model model) {
        Notes notes = new Notes();
        notes.setTicket(ticketService.findByIdTicket(idTicket).get());
        model.addAttribute("notes", notes);
        model.addAttribute("userList", userService.findAllUser());
        return notes;
    }

    public void addNotes(Notes formNotes, RedirectAttributes redirectAttributes, Authentication authentication) {
        String username = authentication.getName(); // Ottiengo l'username
        Optional<User> user = userService.findLogUser(username); // Recupero l'utente dal DB
        formNotes.setUser(user.get());
        notesRepository.save(formNotes);
        redirectAttributes.addFlashAttribute("messageAddNote", "La nota è stata aggiunta");
    }

}
