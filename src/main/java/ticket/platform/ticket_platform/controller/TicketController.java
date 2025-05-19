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
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import jakarta.validation.Valid;
import ticket.platform.ticket_platform.model.Ticket;
import ticket.platform.ticket_platform.service.CategoryService;
import ticket.platform.ticket_platform.service.NotesService;
import ticket.platform.ticket_platform.service.TicketService;
import ticket.platform.ticket_platform.service.UserService;

@Controller
@RequestMapping("/ticket")
public class TicketController {

    private final CategoryService categoryService;
    private final TicketService ticketService;
    private final UserService userService;
    private final NotesService notesService;

    @Autowired
    public TicketController(TicketService ticketService, CategoryService categoryService, UserService userService,
            NotesService notesService) {
        this.ticketService = ticketService;
        this.categoryService = categoryService;
        this.userService = userService;
        this.notesService = notesService;
    }

    /* tables frontpage */
    @GetMapping
    public String frontPage(Model model, @RequestParam(name = "keyword", required = false) String title,
            Authentication authentication) {
        model.addAttribute("ticketList", ticketService.findTicketList(title, authentication));
        model.addAttribute("keyword", title); // per mantenere il valore nel search
        return "frontPage/index";

    }

    @GetMapping("/create")
    public String createTicketGet(Model model) {
        Boolean active = true;
        model.addAttribute("newTicket", new Ticket());
        model.addAttribute("categoryList", categoryService.findAllCategories());
        model.addAttribute("userList", userService.findUserActive(active));
        return "ticket/create";
    }

    @PostMapping("/create")
    public String createTicketPost(@Valid @ModelAttribute("newTicket") Ticket formNewTicket,
            BindingResult bindingResult, Model model, RedirectAttributes redirectAttributes) {
        if (bindingResult.hasErrors()) {
            Boolean active = true;
            model.addAttribute("categoryList", categoryService.findAllCategories());
            model.addAttribute("userList", userService.findUserActive(active));
            return "ticket/create";
        }
        ticketService.create(formNewTicket);

        redirectAttributes.addFlashAttribute("messageAddTicket", "Il ticket è stato aggiunto e assegnato");
        return "redirect:/ticket";
    }

    @GetMapping("/details/{id}")
    public String detailsTicket(@PathVariable("id") Long id, Model model) {
        if (ticketService.ticketDetails(id, model)) {
            return "ticket/details";
        }
        return "error/errorPage";
    }

    @PostMapping("/changeStatus")
    public String changeTicketStatus(@RequestParam Long ticketId,
            @RequestParam String newStatus,
            RedirectAttributes redirectAttributes) {
        ticketService.changeStatusTicket(newStatus, ticketId, redirectAttributes);
        return "redirect:/ticket";
    }

    @PostMapping("/delete/{id}")
    public String deleteTicket(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        ticketService.deleteTicket(id);
        redirectAttributes.addFlashAttribute("deleteMessage", "Il ticket è stato eliminato");
        return "redirect:/ticket";
    }

    @GetMapping("/edit/{id}")
    public String editTicketGet(@PathVariable("id") Long id, Model model) {
        Boolean active = true;
        model.addAttribute("editTicket", ticketService.findByIdTicket(id).get());
        model.addAttribute("categoryList", categoryService.findAllCategories());
        model.addAttribute("userList", userService.findUserActive(active));
        return "ticket/edit";
    }

    @PostMapping("/edit/{id}")
    public String editTicketPost(@Valid @ModelAttribute("editTicket") Ticket formEditTicket, Model model,
            BindingResult bindingResult, RedirectAttributes redirectAttributes) {

        if (bindingResult.hasErrors()) {
            model.addAttribute("categroyList", categoryService.findAllCategories());
            redirectAttributes.addFlashAttribute("errorEdit", "Nessun valore è stato modificato");
            return "ticket/edit";
        }
        redirectAttributes.addFlashAttribute("successEdit", "Il Ticket N. " + formEditTicket.getId()
                + " è stato modificato");
        ticketService.create(formEditTicket);
        return "redirect:/ticket";
    }

}
