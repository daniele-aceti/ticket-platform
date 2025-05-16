package ticket.platform.ticket_platform.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;


@Controller
@RequestMapping
public class FrontPage {

    @GetMapping
    public String frontPage(Model model) {
        return "frontPage/first";
    }
}
