package com.ooline.service;

import com.ooline.model.Category;
import org.springframework.http.ResponseEntity;

import java.util.List;


public interface CategoryService {

    ResponseEntity<Object> addCategory(Category category);

    ResponseEntity<List<Category>> getAllCategorys();

    ResponseEntity<Object> getCategoryById(Integer id);

    ResponseEntity<Object> updateCategoryById(Category category);
}
