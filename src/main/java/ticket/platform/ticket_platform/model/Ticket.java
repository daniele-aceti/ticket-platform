package ticket.platform.ticket_platform.model;

import java.time.LocalDate;
import java.util.List;

import org.springframework.format.annotation.DateTimeFormat;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

@Entity
public class Ticket {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Inserisci un titolo corretto, il minimo dei caratteri è 5")
    @Size(min = 5, max=40)
    @Column(nullable = false)
    private String title;

    @NotBlank(message = "Inserisci una descrizione corretta, il minimo dei caratteri è 5")
    @Size(min = 5, max=100)
    @Column(nullable = false)
    private String ticketDescription;

    @NotBlank(message = "Seleziona almeno una delle due opzioni")
    @Column(nullable = false)
    private String status;

    @NotNull(message="Inserire una data corretta")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate ticketCreationDate;

    @NotNull(message = "Seleziona un utente on-line")
    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @OneToMany(mappedBy = "ticket")
    private List<Notes> notes;

    @ManyToMany
    @JoinTable(
            name = "ticket_category",
            joinColumns = @JoinColumn(name = "ticket_id"),
            inverseJoinColumns = @JoinColumn(name = "category_id")
    )
    private List<Category> categories;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getTicketDescription() {
        return ticketDescription;
    }

    public void setTicketDescription(String ticketDescription) {
        this.ticketDescription = ticketDescription;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public LocalDate getTicketCreationDate() {
        return ticketCreationDate;
    }

    public void setTicketCreationDate(LocalDate ticketCreationDate) {
        this.ticketCreationDate = ticketCreationDate;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public List<Notes> getNotes() {
        return notes;
    }

    public void setNotes(List<Notes> notes) {
        this.notes = notes;
    }

    public List<Category> getCategories() {
        return categories;
    }

    public void setCategories(List<Category> categories) {
        this.categories = categories;
    }

}
