package ticket.platform.ticket_platform.api;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import ticket.platform.ticket_platform.model.Ticket;
import ticket.platform.ticket_platform.service.TicketService;

@RestController
@RequestMapping("/api/ticket")
public class TicketRestController {

    TicketService ticketService;

    public TicketRestController(TicketService ticketService) {
        this.ticketService = ticketService;
    }

    @GetMapping
    public ResponseEntity<List<Ticket>> listTicket(@RequestParam(name = "keyword", required = false) String keyword) {
        List<Ticket> tickets;
        if (keyword != null && !keyword.isBlank()) {
            tickets = ticketService.findTitle(keyword);
        } else {
            tickets = ticketService.findAllTicket();
        }
        if (tickets.isEmpty()) {
            return new ResponseEntity<>(tickets, HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(tickets, HttpStatus.OK);
    }

    @GetMapping("/category")
    public ResponseEntity<List<Ticket>> getCategory(@RequestParam(required = false) String category) {
        List<Ticket> tickets = ticketService.findAllTicket();
        if (category != null && !category.isBlank()) {
            tickets = ticketService.findCategory(category);
            return new ResponseEntity<>(tickets, HttpStatus.OK);
        }
        if (tickets.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }

        return new ResponseEntity<>(tickets, HttpStatus.OK);
    }

    @GetMapping("/status")
    public ResponseEntity<List<Ticket>> getStatus(@RequestParam(required = false) String status) {
        List<Ticket> tickets = ticketService.findAllTicket();
        if (status != null && !status.isBlank()) {
            tickets = ticketService.findStatus(status);
            return new ResponseEntity<>(tickets, HttpStatus.OK);
        }

        return new ResponseEntity<>(tickets, HttpStatus.OK);
    }

}
