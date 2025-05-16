package ticket.platform.ticket_platform.api;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import ticket.platform.ticket_platform.model.Ticket;
import ticket.platform.ticket_platform.repository.TicketRepository;

@RestController
@RequestMapping("/api/ticket")
public class TicketRestController {

    TicketRepository ticketRepository;

    public TicketRestController(TicketRepository ticketRepository) {
        this.ticketRepository = ticketRepository;
    }

    @GetMapping
    public ResponseEntity<List<Ticket>> listTicket(@RequestParam(name = "keyword", required = false) String keyword) {
        List<Ticket> ticketsWithKeyword;
        if (keyword != null && !keyword.isBlank()) {
            ticketsWithKeyword = ticketRepository.findByTitleContainingIgnoreCase(keyword);
        } else {
            ticketsWithKeyword = ticketRepository.findAll();
        }
        return new ResponseEntity<>(ticketsWithKeyword, HttpStatus.OK);
    }

    @GetMapping("/category")
    public List<Ticket> getCategory(@RequestParam(required = false) String category) {
        if (category != null && !category.isBlank()) {
            return ticketRepository.findByCategoriesCategoryNameContainingIgnoreCase(category);
        }
        return ticketRepository.findAll();
    }

    @GetMapping("/status")
    public List<Ticket> getStatus(@RequestParam(required = false) String status) {
        if (status != null && !status.isBlank()) {
            return ticketRepository.findByStatusContainingIgnoreCase(status);
        }
        return ticketRepository.findAll();
    }

}
