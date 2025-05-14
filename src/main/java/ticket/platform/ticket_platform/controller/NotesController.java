package ticket.platform.ticket_platform.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import jakarta.validation.Valid;
import ticket.platform.ticket_platform.model.Notes;
import ticket.platform.ticket_platform.repository.NotesRepository;
import ticket.platform.ticket_platform.repository.TicketRepository;
import ticket.platform.ticket_platform.repository.UserRepository;


@Controller
public class NotesController {

    private final UserRepository userRepository;

    private NotesRepository notesRepository;
    private TicketRepository ticketRepository;

    @Autowired
    public NotesController(NotesRepository notesRepository, TicketRepository ticketRepository, UserRepository userRepository) {
        this.notesRepository = notesRepository;
        this.ticketRepository = ticketRepository;
        this.userRepository = userRepository;
    }

    @GetMapping("/add_notes/{id}")
    public String addNotesGet(@PathVariable("id") Long idTikcet, Model model) {
        Notes notes = new Notes();
        notes.setTicket(ticketRepository.findById(idTikcet).get());
        model.addAttribute("notes", notes);
        model.addAttribute("userList", userRepository.findAll());

        return "notes/edit";
    }

    @PostMapping("/add_notes")
    public String addNotesPost(@Valid @ModelAttribute Notes formNotes, Model model, BindingResult bindingResult) {
           if (bindingResult.hasErrors()) {
            return "notes/edit";
        }
        notesRepository.save(formNotes);
        return "notes/edit";
    }
    
    
}
