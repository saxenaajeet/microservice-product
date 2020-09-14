package com.ooline.service;

import com.ooline.model.Category;
import org.springframework.http.ResponseEntity;

public interface ProductCategoryService {

    ResponseEntity<Object> addSubCategory(Category category);

    ResponseEntity<Category> getAllProductSubCategorys(Integer categoryId);

    ResponseEntity<Object> getProductSubCategoryById(Integer categoryId, Integer id);

    ResponseEntity<Object> getAllProducts(Integer categoryId, Integer id);

    ResponseEntity<Object> getProductById(Integer categoryId, Integer id, Integer productId);

    ResponseEntity<Object> addProduct(Category category);

    //public ResponseEntity<Object> updateProductCategoryById(String id, Category category);
}

