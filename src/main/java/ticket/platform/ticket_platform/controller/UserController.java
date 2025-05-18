package ticket.platform.ticket_platform.controller;

import java.util.List;

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
import ticket.platform.ticket_platform.model.Role;
import ticket.platform.ticket_platform.model.User;
import ticket.platform.ticket_platform.repository.RoleRepository;
import ticket.platform.ticket_platform.repository.UserRepository;

@Controller
@RequestMapping("/user")
public class UserController {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;

    @Autowired
    public UserController(UserRepository userRepository, RoleRepository roleRepository) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
    }

    @GetMapping("/create")
    public String userCreateGet(Model model) {
        Boolean active = false;
        model.addAttribute("newUser", new User());
        model.addAttribute("userList", userRepository.findByActive(active));
        return "operatore/create";
    }

    @PostMapping("/create")
    public String userCreatePost(@Valid @ModelAttribute("newUser") User formNewUser, 
            BindingResult bindingResult,Model model, RedirectAttributes redirectAttributes) {
        if (bindingResult.hasErrors()) {
             model.addAttribute("newUser", formNewUser);
            return "operatore/create";
        }
        Role operatore = roleRepository.findByRoleName("OPERATORE");
        formNewUser.setRole(List.of(operatore));//mi da una nuova lista con solo un elemento OPERATORE
        formNewUser.setPassword("{noop}" + formNewUser.getPassword());
        userRepository.save(formNewUser);
        redirectAttributes.addFlashAttribute("addUser", "L'utente: " + formNewUser.getName() + " è stato aggiunto");
        return "redirect:/ticket";
    }

}
