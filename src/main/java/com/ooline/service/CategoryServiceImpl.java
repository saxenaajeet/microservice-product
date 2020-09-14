package com.ooline.service;

import com.ooline.OolineApplication;
import com.ooline.exceptions.BadRequestException;
import com.ooline.exceptions.DefaultExceptionHandler;
import com.ooline.exceptions.ResourceNotFoundException;
import com.ooline.model.Category;
import com.ooline.model.EmptyJsonBody;
import com.ooline.model.ResponseObject;
import com.ooline.repository.CategoryRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Qualifier("categoryServiceImpl")
public class CategoryServiceImpl implements CategoryService {

    private CategoryRepository categoryRepository;
    private DefaultExceptionHandler defaultExceptionHandler;
    private SequenceGeneratorService sequenceGeneratorService;
    private static final Logger logger = LoggerFactory.getLogger(OolineApplication.class);

    @Autowired
    public void setCategoryRepository(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }

    @Autowired
    public void setDefaultExceptionHandler(DefaultExceptionHandler defaultExceptionHandler) {
        this.defaultExceptionHandler = defaultExceptionHandler;
    }

    @Autowired
    public void setSequenceGeneratorService(SequenceGeneratorService sequenceGeneratorService) {
        this.sequenceGeneratorService = sequenceGeneratorService;
    }

    @Override
    public ResponseEntity<Object> addCategory(Category category) {
        logger.info("addCategory method called with param {}", category);

        Category categoryObjFromDB = categoryRepository.findByCategoryName(category.getCategoryName());
        ResponseEntity<Object> returnResponseEntityObject = validateInputCategory(category, categoryObjFromDB);
        if (returnResponseEntityObject != null)
            return returnResponseEntityObject;
        Category returnObject = null;
        try {
            category.setNewRecord(true);
            category.setId(sequenceGeneratorService.generateSequence(Category.SEQUENCE_NAME));
            returnObject = categoryRepository.save(category);
        } catch (Exception ex) {
            return defaultExceptionHandler.handleBadRequestException(new BadRequestException(ex.getMessage()));
        }
        logger.info("If return object is not null then send the response");
        if (returnObject != null) {
            return new ResponseEntity<>(new ResponseObject("Category created successfully", HttpStatus.CREATED), HttpStatus.CREATED);
        }
        return null;
    }

    @Override
    public ResponseEntity<List<Category>> getAllCategorys() {
        logger.info("Method called getAllCategorys");
        List<Category> categoryList = categoryRepository.findAll();
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/json");
        return new ResponseEntity<>(categoryList, headers, HttpStatus.OK);
    }

    @Override
    public ResponseEntity<Object> getCategoryById(Integer id) {
        logger.info("Method called getCategoryById in the system with the id = {}", id);
        Category category = categoryRepository.findById(id).orElse(null);
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/json");
        if (category == null) {
            logger.info("Category is null, return an Empty object");
            return new ResponseEntity<>(new EmptyJsonBody(), headers, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(category, headers, HttpStatus.OK);
        }
    }

    @Override
    public ResponseEntity<Object> updateCategoryById(Category category) {
        logger.info("Method updateCategoryById called to update the category object");
        Integer inputCategoryId = category.getId();
        if (inputCategoryId == null || inputCategoryId == 0) {
            logger.info("Cannot update Category without an id");
            return defaultExceptionHandler.handleBadRequestException(new BadRequestException("Cannot update Category without an id"));
        }
        if (category.getProductSubCategorys() != null) {
            try {
                throw new BadRequestException("API allow only update of Category");
            } catch (BadRequestException ex) {
                logger.info("API allow only update of Category");
                return defaultExceptionHandler.handleBadRequestException(ex);
            }
        }
        Category existingCategory = categoryRepository.findById(inputCategoryId).orElse(null);
        if (existingCategory != null) {
            existingCategory.setNewRecord(false);
            if (existingCategory.getCategoryImage() != null)
                existingCategory.setCategoryImage(category.getCategoryImage());
            if (existingCategory.getCategoryName() != null)
                existingCategory.setCategoryName(category.getCategoryName());
            return new ResponseEntity<>(categoryRepository.save(existingCategory), HttpStatus.OK);
        } else {
            logger.info("Category with the Id does not exist");
            return defaultExceptionHandler.handleResourceNotFoundException(new ResourceNotFoundException("Category with the Id does not exist"));
        }
    }

    private ResponseEntity<Object> validateInputCategory(Category category, Category categoryFromDB) {
        logger.info("Method validateInputCategory called to validate the category input object");
        if (category.getProductSubCategorys() != null && category.getProductSubCategorys().size() != 0) {
            logger.info("API allow only addition of Category");
            return defaultExceptionHandler.handleBadRequestException(new BadRequestException("API allow only addition of Category"));
        }

        if (category != null) {
            if (category.getCategoryName().equals("") || category.getCategoryName() == null) {
                logger.info("Category Name Cannot be null or Empty");
                return defaultExceptionHandler.handleBadRequestException(new BadRequestException("Category Name Cannot be null or Empty"));
            }
            if (categoryFromDB != null) {
                logger.info("Category Name already exist in the Database");
                return defaultExceptionHandler.handleBadRequestException(new BadRequestException("Category Name already exist in the Database"));
            }
        }

        if (!category.getCategoryType().equalsIgnoreCase("product")) {
            logger.info("The category type should be product");
            return defaultExceptionHandler.handleBadRequestException(new BadRequestException("The category type should be product"));
        }
        return null;
    }
}
