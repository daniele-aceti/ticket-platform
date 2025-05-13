package ticket.platform.ticket_platform.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import ticket.platform.ticket_platform.model.Notes;
import ticket.platform.ticket_platform.repository.NotesRepository;
import ticket.platform.ticket_platform.repository.TicketRepository;


@Controller
public class NotesController {

    private NotesRepository notesRepository;
    private TicketRepository ticketRepository;

    @Autowired
    public NotesController(NotesRepository notesRepository, TicketRepository ticketRepository) {
        this.notesRepository = notesRepository;
        this.ticketRepository = ticketRepository;
    }

    @GetMapping("/add_notes/{id}")
    public String addNotes(@PathVariable("id") Long idTikcet, Model model) {
        Notes notes = new Notes();
        notes.setTicket(ticketRepository.findById(idTikcet).get());
        model.addAttribute("notes", notes);
        return "notes/edit";
    }
    
}
