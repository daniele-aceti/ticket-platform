package ticket.platform.ticket_platform.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import jakarta.validation.Valid;
import ticket.platform.ticket_platform.model.Notes;
import ticket.platform.ticket_platform.service.NotesService;

@Controller
public class NotesController {

    private final NotesService notesService;

    @Autowired
    public NotesController(NotesService notesService) {
        this.notesService = notesService;
    }

    @GetMapping("/add_notes/{id}")
    public String addNotesGet(@PathVariable("id") Long idTicket, Model model) {
        notesService.viewNotes(idTicket, model);
        return "notes/create";
    }

    @PostMapping("/add_notes")
    public String addNotesPost(@Valid @ModelAttribute Notes formNotes, BindingResult bindingResult, Model model,
            RedirectAttributes redirectAttributes, Authentication authentication) {
         if (bindingResult.hasErrors()) {
            return "notes/create";
        }
        notesService.addNotes(formNotes, redirectAttributes, authentication);
        return "redirect:/ticket";
    }

}
