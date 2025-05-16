package ticket.platform.ticket_platform.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
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
import ticket.platform.ticket_platform.model.Category;
import ticket.platform.ticket_platform.model.Ticket;
import ticket.platform.ticket_platform.repository.CategoryRepository;
import ticket.platform.ticket_platform.repository.TicketRepository;

@Controller
@RequestMapping("/category")
public class CategoryController {

    CategoryRepository categoryRepository;
    TicketRepository ticketRepository;

    @Autowired
    public CategoryController(CategoryRepository categoryRepository, TicketRepository ticketRepository) {
        this.categoryRepository = categoryRepository;
        this.ticketRepository = ticketRepository;
    }

    @GetMapping("/create")
    public String createCategoryGet(Model model) {
        model.addAttribute("categoryList", categoryRepository.findAll());
        model.addAttribute("newCategory", new Category());
        return "category/create";
    }

    @PostMapping("/create")
    public String createCategoryPost(@Valid @ModelAttribute Category formnewCategory, Model model,
            BindingResult bindingResult, RedirectAttributes redirectAttributes) {
        List<Category> oldCategoryList = categoryRepository.findAll();
        if (bindingResult.hasErrors()) {
            return "redirect:/category/create";
        }
        //cerca categoria gia esistente
        boolean categoryExists = false;

        for (Category oldCategory : oldCategoryList) {
            if (oldCategory.getCategoryName().equalsIgnoreCase(formnewCategory.getCategoryName())) {
                categoryExists = true;
                break;
            }
        }

        if (categoryExists) {
            redirectAttributes.addFlashAttribute("categoryError",
                    "Stai tentando di inserire due volte la stessa categoria");
        } else {
            categoryRepository.save(formnewCategory);
            redirectAttributes.addFlashAttribute("addCategory",
                    "La nuova categoria: " + formnewCategory.getCategoryName() + " è stata aggiunta");
        }

        return "redirect:/category/create";
    }

    @PostMapping("/delete/{id}")
    public String deleteCategory(@PathVariable Long id, Model model,
            RedirectAttributes redirectAttributes) {
        Category category = categoryRepository.findById(id).get();
        for (Ticket ticket : category.getTickets()) {
            ticket.getCategories().remove(category);
        }
        categoryRepository.deleteById(id);
        redirectAttributes.addFlashAttribute("deleteCategory", "La categoria è stata rimossa");
        return "redirect:/category/create";
    }

}
