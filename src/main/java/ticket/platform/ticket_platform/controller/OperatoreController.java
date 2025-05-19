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
import ticket.platform.ticket_platform.model.User;
import ticket.platform.ticket_platform.service.OperatoreService;
import ticket.platform.ticket_platform.service.UserService;

@Controller
@RequestMapping("/operatore")
public class OperatoreController {

    private final OperatoreService operatoreService;
    private final UserService userService;


    @Autowired
    public OperatoreController(OperatoreService operatoreService, UserService userService) {
        this.operatoreService = operatoreService;
        this.userService = userService;
    }

    @GetMapping("/edit/{id}")
    public String editOperatoreGet(@PathVariable Long id, Model model, Authentication authentication) {
        String username = authentication.getName();
        User user = userService.findLogUser(username).get();
        model.addAttribute("operatore", user);
        return "operatore/edit";
    }

    @PostMapping("/edit/{id}")
    public String editOperatorePost(@Valid @PathVariable Long id, @ModelAttribute User newDataUser,
            BindingResult bindingResult, Model model, RedirectAttributes redirectAttributes) {
        if (bindingResult.hasErrors()) {
            redirectAttributes.addFlashAttribute("ErrorUser", "I dati non sono stati aggiornati");
            return "operatore/edit";
        }
        if (operatoreService.userEdit(id, newDataUser, bindingResult, model, redirectAttributes)) {
            return "redirect:/operatore/edit/" + id;
        }
        return "redirect:/ticket";
    }

}
