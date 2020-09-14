package com.ooline.api.products;

import com.fasterxml.jackson.annotation.JsonView;
import com.ooline.OolineApplication;
import com.ooline.model.Category;
import com.ooline.model.views.CategoryView;
import com.ooline.service.ProductCategoryService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.NonNull;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;


@RequestMapping("ooline/api/v1")
@RestController
public class ProductController {

    private static final Logger logger = LoggerFactory.getLogger(OolineApplication.class);

    @Autowired
    @Qualifier("productCategoryServiceImpl")
    private ProductCategoryService productCategoryService;

    @PostMapping("/productsubcategory")
    public ResponseEntity<Object> addProductSubCategory(@Valid @NonNull @RequestBody Category category) {
        logger.info("Method addProductSubCategory called");
        return productCategoryService.addSubCategory(category);
    }

    @JsonView(CategoryView.CategoryWithSubCategory.class)
    @GetMapping("/category/{categoryId}/productsubcategory")
    public ResponseEntity<Category> getAllProductCategorys(@PathVariable("categoryId") Integer categoryId) {
        logger.info("Method getAllProductCategorys called");
        return productCategoryService.getAllProductSubCategorys(categoryId);
    }

    @JsonView(CategoryView.CategoryWithSubCategory.class)
    @GetMapping("/category/{categoryId}/productsubcategory/{prodsubid}")
    public ResponseEntity<Object> getProductCategoryById(@PathVariable("categoryId") Integer categoryId, @PathVariable("prodsubid") Integer id) {
        logger.info("Method getProductCategoryById called");
        return productCategoryService.getProductSubCategoryById(categoryId, id);
    }

    @JsonView(CategoryView.ProductsWithCategory.class)
    @GetMapping("/category/{categoryId}/productsubcategory/{prodsubid}/product")
    public ResponseEntity<Object> getAllProducts(@PathVariable("categoryId") Integer categoryId, @PathVariable("prodsubid") Integer id) {
        logger.info("Method getAllProducts called");
        return productCategoryService.getAllProducts(categoryId, id);
    }

    @JsonView(CategoryView.ProductsWithCategory.class)
    @GetMapping("/category/{categoryId}/productsubcategory/{prodsubid}/product/{productId}")
    public ResponseEntity<Object> getProductById(@PathVariable("categoryId") Integer categoryId, @PathVariable("prodsubid") Integer id, @PathVariable("productId") Integer productId) {
        logger.info("Method getProductById called");
        return productCategoryService.getProductById(categoryId, id, productId);
    }

    @PostMapping("/product")
    public ResponseEntity<Object> addProduct(@Valid @NonNull @RequestBody Category category) {
        logger.info("Method addProduct called to add the products");
        return productCategoryService.addProduct(category);
    }

}
