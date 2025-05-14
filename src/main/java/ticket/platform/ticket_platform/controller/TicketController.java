package ticket.platform.ticket_platform.controller;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import jakarta.validation.Valid;
import ticket.platform.ticket_platform.model.Ticket;
import ticket.platform.ticket_platform.repository.CategoryRepository;
import ticket.platform.ticket_platform.repository.NotesRepository;
import ticket.platform.ticket_platform.repository.TicketRepository;
import ticket.platform.ticket_platform.repository.UserRepository;

@Controller
@RequestMapping("/ticket")
public class TicketController {

    private NotesRepository notesRepository;
    private TicketRepository ticketRepository;
    private CategoryRepository categoryRepository;
    private UserRepository userRepository;

    @Autowired
    public TicketController(TicketRepository ticketRepository, CategoryRepository categoryRepository,
            UserRepository userRepository, NotesRepository notesRepository) {
        this.ticketRepository = ticketRepository;
        this.categoryRepository = categoryRepository;
        this.userRepository = userRepository;
        this.notesRepository = notesRepository;
    }

    @GetMapping
    public String frontPage(Model model, @RequestParam(name = "keyword", required = false) String title) {
        if (title != null && !title.isBlank()) {
            model.addAttribute("ticketList", ticketRepository.findByTitleContainingIgnoreCase(title));
        } else {
            model.addAttribute("ticketList", ticketRepository.findAll());
        }
        model.addAttribute("keyword", title); // per mantenere il valore nel form
        model.addAttribute("userList", userRepository.findAll());
        return "frontPage/index";

    }

    @GetMapping("/create")
    public String createTicketGet(Model model) {
        model.addAttribute("newTicket", new Ticket());
        model.addAttribute("categoryList", categoryRepository.findAll());
        model.addAttribute("userList", userRepository.findAll());
        return "ticket/create";
    }

    @PostMapping("/create")
    public String createTicketPost(@Valid @ModelAttribute("newTicket") Ticket formNewTicket, BindingResult bindingResult,
            Model model, RedirectAttributes redirectAttributes) {
        if (bindingResult.hasErrors()) {
            return "ticket/create";
        }
        ticketRepository.save(formNewTicket);

        redirectAttributes.addFlashAttribute("messageAddNote", "Il ticket è stato aggiunto e assegnato");
         return "redirect:/ticket";
    }

    @GetMapping("/details/{id}")
    public String detailsTicket(@PathVariable("id") Long id, Model model) {
        Optional<Ticket> optTicket = ticketRepository.findById(id);
        if (optTicket.isPresent()) {
            model.addAttribute("detailsTicket", optTicket.get());

            return "ticket/details";
        }
        return "error/errorPage";
    }

}
