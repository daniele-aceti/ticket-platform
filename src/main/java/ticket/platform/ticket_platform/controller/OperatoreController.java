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
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import jakarta.validation.Valid;
import ticket.platform.ticket_platform.model.Ticket;
import ticket.platform.ticket_platform.model.User;
import ticket.platform.ticket_platform.repository.TicketRepository;
import ticket.platform.ticket_platform.repository.UserRepository;

@Controller
@RequestMapping("/operatore")
public class OperatoreController {

    TicketRepository ticketRepository;
    UserRepository userRepository;

    @Autowired
    public OperatoreController(UserRepository userRepository, TicketRepository ticketRepository) {
        this.ticketRepository = ticketRepository;
        this.userRepository = userRepository;
    }

    @GetMapping("/edit/{id}")
    public String editOperatoreGet(@PathVariable Long id, Model model, Authentication authentication) {
        String username = authentication.getName();
        User user = userRepository.findByEmail(username).get();

        if (user == null) {
            return "error/errorPage";
        }
        model.addAttribute("operatore", user);
        return "operatore/edit";
    }

   @PostMapping("/edit/{id}")
public String postMethodName(@Valid @PathVariable Long id, @ModelAttribute User newDataUser, Model model,
        RedirectAttributes redirectAttributes, BindingResult bindingResult) {
    if (bindingResult.hasErrors()) {
        redirectAttributes.addFlashAttribute("ErrorUser", "I dati non sono stati aggiornati");
        return "operatore/edit";
    }

    User user = userRepository.findById(id).get();
    if (user == null) {
        redirectAttributes.addFlashAttribute("ErrorUser", "Utente non trovato");
        return "redirect:/operatore/edit/" + id;
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
    Ticket ticketUser = ticketRepository.findById(id).orElse(null);
    if (ticketUser != null && ticketUser.getStatus().equals("Completato")) {
        if (user.isActive() != newDataUser.isActive()) {
            user.setActive(newDataUser.isActive());
            changed = true;
        }
    } else {
        redirectAttributes.addFlashAttribute("ErrorActive", "Non puoi scollegarti ci sono ancora dei ticket da chiudere");
        return "redirect:/operatore/edit/" + id;
    }

    // Password (solo se modificata e non vuota)
    if (newDataUser.getPassword() != null && !newDataUser.getPassword().isBlank()) {
        //imposta password criptata
        user.setPassword("{noop}" + newDataUser.getPassword());
        changed = true;
    }

    if (changed) {
        userRepository.save(user);
        redirectAttributes.addFlashAttribute("messageEditUser", "I tuoi dati sono stati aggiornati");
    }

    return "redirect:/ticket";
}


}
