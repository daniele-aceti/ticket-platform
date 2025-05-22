package ticket.platform.ticket_platform.controller;

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
import ticket.platform.ticket_platform.service.CategoryService;

@Controller
@RequestMapping("/category")
public class CategoryController {

    private final CategoryService categoryService;

    @Autowired
    public CategoryController(CategoryService categoryService) {
        this.categoryService = categoryService;
    }

    @GetMapping("/create")
    public String createCategoryGet(Model model) {
        model.addAttribute("categoryList", categoryService.findAllCategories());
        model.addAttribute("newCategory", new Category());
        return "category/create";
    }

    @PostMapping("/create")
    public String createCategoryPost(@Valid @ModelAttribute("newCategory") Category formnewCategory,
            BindingResult bindingResult, Model model, RedirectAttributes redirectAttributes) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("categoryList", categoryService.findAllCategories());
            return "category/create";
        }
        categoryService.newCategoryAndCheck(formnewCategory, redirectAttributes, bindingResult, model);
        return "redirect:/category/create";
    }

    @PostMapping("/delete/{id}")
    public String deleteCategory(@PathVariable Long id, Model model,
            RedirectAttributes redirectAttributes) {
        categoryService.deleteCategory(id);
        redirectAttributes.addFlashAttribute("deleteCategory", "La categoria è stata rimossa");
        return "redirect:/category/create";
    }

}
