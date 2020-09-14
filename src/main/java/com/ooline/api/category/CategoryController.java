package com.ooline.api.category;

import com.fasterxml.jackson.annotation.JsonView;
import com.ooline.model.Category;
import com.ooline.model.views.CategoryView;
import com.ooline.service.CategoryService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.NonNull;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;


@RequestMapping("ooline/api/v1")
@RestController
public class CategoryController {
    private final Logger logger = LoggerFactory.getLogger(getClass());
    private CategoryService categoryService;

    @Autowired
    @Qualifier("categoryServiceImpl")
    public void setCategoryService(CategoryService categoryService) {
        this.categoryService = categoryService;
    }

    @PostMapping("/category")
    public ResponseEntity<Object> addCategory(@Valid @NonNull @RequestBody Category category) {
        logger.info("Method addCategory called to add the Category");
        return categoryService.addCategory(category);
    }

    @GetMapping("/category")
    @JsonView(CategoryView.MinimalCategory.class)
    public ResponseEntity<List<Category>> getCategorys() {
        logger.info("Method getCategorys called");
        return categoryService.getAllCategorys();
    }

    @GetMapping("/category/{id}")
    @JsonView(CategoryView.MinimalCategory.class)
    public ResponseEntity<Object> getCategoryById(@PathVariable("id") Integer id) {
        logger.info("Method getCategoryById called");
        return categoryService.getCategoryById(id);
    }

    @PutMapping(path = "/category")
    public ResponseEntity<Object> updateCategoryById(@Valid @NonNull @RequestBody Category category) {
        logger.info("Method updateCategoryById called");
        return categoryService.updateCategoryById(category);
    }
}
