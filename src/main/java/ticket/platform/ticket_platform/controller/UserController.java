package ticket.platform.ticket_platform.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import jakarta.validation.Valid;
import ticket.platform.ticket_platform.model.User;
import ticket.platform.ticket_platform.service.UserService;

@Controller
@RequestMapping("/user")
public class UserController {

    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/create")
    public String userCreateGet(Model model) {
        Boolean active = false;
        model.addAttribute("newUser", new User());
        model.addAttribute("userList", userService.findUserActive(active));
        return "operatore/create";
    }

    @PostMapping("/create")
    public String userCreatePost(@Valid @ModelAttribute("newUser") User formNewUser, 
            BindingResult bindingResult,Model model, RedirectAttributes redirectAttributes) {
        if (bindingResult.hasErrors()) {
             model.addAttribute("newUser", formNewUser);
            return "operatore/create";
        }
        userService.operatoreCreate(formNewUser);
        redirectAttributes.addFlashAttribute("addUser", "L'utente: " + formNewUser.getName() + " è stato aggiunto");
        return "redirect:/ticket";
    }

}
