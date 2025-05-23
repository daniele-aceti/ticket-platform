package ticket.platform.ticket_platform.model;

import java.time.LocalDate;

import org.springframework.format.annotation.DateTimeFormat;

import com.fasterxml.jackson.annotation.JsonBackReference;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

@Entity
public class Notes {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    @Size(min = 10, message = "Inserisci una descrizione corretta, il minimo dei caratteri è 10")
    @Column(nullable = false)
    private String notesDescription;

    @NotNull(message = "Seleziona una data")
    @DateTimeFormat(pattern = "yyy-MM-dd")
    @Column(nullable = false)
    private LocalDate notesCreationDate;

    @ManyToOne
    @JoinColumn(name = "ticket_id", nullable = false)
    @JsonBackReference 
    private Ticket ticket;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNotesDescription() {
        return notesDescription;
    }

    public void setNotesDescription(String notesDescription) {
        this.notesDescription = notesDescription;
    }

    public LocalDate getNotesCreationDate() {
        return notesCreationDate;
    }

    public void setNotesCreationDate(LocalDate notesCreationDate) {
        this.notesCreationDate = notesCreationDate;
    }

    public Ticket getTicket() {
        return ticket;
    }

    public void setTicket(Ticket ticket) {
        this.ticket = ticket;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

}
