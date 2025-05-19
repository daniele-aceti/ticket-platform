package ticket.platform.ticket_platform.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import ticket.platform.ticket_platform.model.Category;
import ticket.platform.ticket_platform.model.Ticket;
import ticket.platform.ticket_platform.repository.CategoryRepository;

@Service
public class CategoryService {

    private final CategoryRepository categoryRepository;

    @Autowired
    public CategoryService(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }

    public List<Category> findAllCategories() {
        return categoryRepository.findAll();
    }

    public Category create(Category category) {
        return categoryRepository.save(category);
    }

    public void deleteCategory(Long id) {
        Category category = findByIdCategory(id).get();
        for (Ticket ticket : category.getTickets()) {
            ticket.getCategories().remove(category);
        }
        categoryRepository.deleteById(id);
    }

    public Optional<Category> findByIdCategory(Long id) {
        return categoryRepository.findById(id);
    }

    public List<Category> newCategoryAndCheck(Category formnewCategory, RedirectAttributes redirectAttributes,
            BindingResult bindingResult, Model model) {
        List<Category> oldCategoryList = findAllCategories();

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
            create(formnewCategory);
            redirectAttributes.addFlashAttribute("addCategory",
                    "La nuova categoria: " + formnewCategory.getCategoryName() + " è stata aggiunta");
        }

        return oldCategoryList;
    }

}
