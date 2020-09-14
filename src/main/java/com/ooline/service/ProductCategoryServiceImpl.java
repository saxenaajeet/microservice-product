package com.ooline.service;

import com.ooline.OolineApplication;
import com.ooline.exceptions.BadRequestException;
import com.ooline.exceptions.DefaultExceptionHandler;
import com.ooline.exceptions.ResourceNotFoundException;
import com.ooline.model.Category;
import com.ooline.model.ResponseObject;
import com.ooline.model.product.Product;
import com.ooline.model.product.ProductSubCategory;
import com.ooline.repository.CategoryRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;


@Service
@Qualifier("productCategoryServiceImpl")
@Configuration
public class ProductCategoryServiceImpl implements ProductCategoryService {
    private static final Logger logger = LoggerFactory.getLogger(OolineApplication.class);

    private DefaultExceptionHandler defaultExceptionHandler;
    private CategoryRepository categoryRepository;
    private SequenceGeneratorService sequenceGeneratorService;

    @Autowired
    public void setSequenceGeneratorService(SequenceGeneratorService sequenceGeneratorService) {
        this.sequenceGeneratorService = sequenceGeneratorService;
    }

    @Autowired
    public void setDefaultExceptionHandler(DefaultExceptionHandler defaultExceptionHandler) {
        this.defaultExceptionHandler = defaultExceptionHandler;
    }

    @Autowired
    public void setCategoryRepository(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }


    @Override
    public ResponseEntity<Object> addSubCategory(Category category) {
        Integer categoryId = category.getId();
        if (categoryId == null) {
            return defaultExceptionHandler.handleBadRequestException(new BadRequestException("Not a valid Json - category \"Id\" missing"));
        }
        logger.info("Category Id is {}", categoryId);
        Category categoryFromDB = categoryRepository.findById(categoryId).orElse(null);
        ResponseEntity<Object> categoryValidationResponse = validateInputCategory(category, categoryFromDB);
        if (categoryValidationResponse != null)
            return categoryValidationResponse;
        List<ProductSubCategory> inputListOfProductSubCategories = null;
        if (category != null)
            inputListOfProductSubCategories = category.getProductSubCategorys();
        ResponseEntity<Object> responseCreationObject = validateProductSubCategoryList(inputListOfProductSubCategories, categoryFromDB.getProductSubCategorys());
        if (responseCreationObject != null)
            return responseCreationObject;

        logger.info("Modify the objects in the input Product Sub Category List");
        for (ProductSubCategory productSubCategory : inputListOfProductSubCategories) {
            productSubCategory.setId(sequenceGeneratorService.generateSequence(ProductSubCategory.SEQUENCE_NAME));
            productSubCategory.setNewRecord(true);
        }
        logger.info("Add the input Product SubCategory list to the List from DB");
        if (categoryFromDB.getProductSubCategorys() != null) {
            logger.info("Product category from DB = {}", categoryFromDB.getProductSubCategorys());
            categoryFromDB.getProductSubCategorys().addAll(inputListOfProductSubCategories);
        } else {
            logger.info("categoryFromDB.getProductSubCategorys() value is null.. Add the input list to the Category Object");
            categoryFromDB.setProductSubCategorys(inputListOfProductSubCategories);
        }

        logger.info("Saving the product sub categories to the DB");
        Category returnObject = categoryRepository.save(categoryFromDB);

        if (returnObject != null) {
            logger.info("return object is not null then send the response");
            return new ResponseEntity<>(new ResponseObject("Product sub category created successfully", HttpStatus.CREATED), HttpStatus.CREATED);
        }
        return null;
    }

    @Override
    public ResponseEntity<Category> getAllProductSubCategorys(Integer categoryId) {
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/json");
        logger.info("Return Category object in the ResponseEntity");
        Category category = categoryRepository.findById(categoryId).orElse(null);
        return new ResponseEntity<>(category, HttpStatus.OK);
    }

    @Override
    public ResponseEntity<Object> getProductSubCategoryById(Integer categoryId, Integer id) {
        Category category = getProductCategoryById(categoryId, id);
        if (category == null) {
            return defaultExceptionHandler.handleResourceNotFoundException(new ResourceNotFoundException("Category Non existent"));
        }
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/json");
        return new ResponseEntity<>(category, headers, HttpStatus.OK);
    }

    @Override
    public ResponseEntity<Object> getAllProducts(Integer categoryId, Integer id) {
        Category category = getProductCategoryById(categoryId, id);
        if (category == null) {
            return defaultExceptionHandler.handleResourceNotFoundException(new ResourceNotFoundException("Category Non existent"));
        }
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/json");
        return new ResponseEntity<>(category, headers, HttpStatus.OK);

    }

    @Override
    public ResponseEntity<Object> getProductById(Integer categoryId, Integer id, Integer productId) {
        Category category = getProductCategoryById(categoryId, id);
        if (category == null) {
            return defaultExceptionHandler.handleResourceNotFoundException(new ResourceNotFoundException("Category Non existent"));
        }

        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/json");

        ProductSubCategory productSubCategory = category.getProductSubCategorys().get(0);
        List<Product> products = productSubCategory.getProducts();
        List<Product> newProductList = new ArrayList<>();
        products.stream().forEach(product -> {
            if (product != null && product.getId().equals(productId)) {
                newProductList.add(product);
            }
        });
        productSubCategory.setProducts(newProductList);
        return new ResponseEntity<>(category, headers, HttpStatus.OK);
    }

    @Override
    public ResponseEntity<Object> addProduct(Category category) {
        Integer inputCategoryId = category.getId();
        Category categoryFromDB = null;
        if (inputCategoryId != null)
            categoryFromDB = categoryRepository.findById(inputCategoryId).orElse(null);
        logger.info("Category Object is {}", categoryFromDB);
        logger.info("Validate top level category in the input Json");
        ResponseEntity<Object> errorResponseEntity = validateInputCategory(category, categoryFromDB);
        if (errorResponseEntity != null)
            return errorResponseEntity;
        ProductSubCategory inputProductSubCategory = category.getProductSubCategorys().get(0);
        List<ProductSubCategory> productSubCategoryListFromDB = categoryFromDB.getProductSubCategorys();
        errorResponseEntity = validateProductSubCategory(inputProductSubCategory, productSubCategoryListFromDB);
        if (errorResponseEntity != null)
            return errorResponseEntity;

        List<Product> listOfProductsFromDB = null;
        for (ProductSubCategory productSubCategory : productSubCategoryListFromDB) {
            if (productSubCategory.getId() == inputProductSubCategory.getId()) {
                listOfProductsFromDB = productSubCategory.getProducts();
            }
        }
        List<Product> inputProductList = inputProductSubCategory.getProducts();
        ResponseEntity<Object> responseEntity = validateProductList(inputProductList, listOfProductsFromDB);
        if (responseEntity != null)
            return responseEntity;
        logger.info("Add the sequences and isNew value of true to all the product objects");
        for (Product inputProduct : inputProductList) {
            inputProduct.setId(sequenceGeneratorService.generateSequence(Product.SEQUENCE_NAME));
            inputProduct.setNewRecord(true);
        }
        logger.info("Merge the product input list to the list from DB");
        if (listOfProductsFromDB != null) {
            listOfProductsFromDB.addAll(inputProductList);
            for (Product product : listOfProductsFromDB) {
                logger.info("Product name ={}", product.getName());
            }
        } else {
            for (ProductSubCategory productSubCategory : productSubCategoryListFromDB) {
                if (productSubCategory.getId() == inputProductSubCategory.getId()) {
                    productSubCategory.setProducts(inputProductList);
                }
            }
        }
        logger.info("Saving the products to the DB");
        Category returnObject = categoryRepository.save(categoryFromDB);

        if (returnObject != null) {
            logger.info("Return object is not null then send the response");
            return new ResponseEntity<>(new ResponseObject("Products created successfully", HttpStatus.CREATED), HttpStatus.CREATED);
        }
        return null;
    }


    private ResponseEntity<Object> validateInputCategory(Category category, Category categoryFromDB) {
        if (category.getId() == null) {
            return defaultExceptionHandler.handleBadRequestException(new BadRequestException("Category Id is missing in the Json"));
        }
        logger.info("Category Object is {}", category);
        if (!category.getCategoryType().equalsIgnoreCase("product")) {
            return defaultExceptionHandler.handleBadRequestException(new BadRequestException("Category Type should be product"));
        }
        if (categoryFromDB == null) {
            return defaultExceptionHandler.handleBadRequestException(new BadRequestException("Category Non existent"));
        }
        if (!categoryFromDB.getCategoryName().equals(category.getCategoryName())) {
            return defaultExceptionHandler.handleResourceNotFoundException(new ResourceNotFoundException("The categoryName is not correct with the Id provided"));
        }
        return null;
    }

    private ResponseEntity<Object> validateProductSubCategoryList(List<ProductSubCategory> inputProductSubCategoryList, List<ProductSubCategory> listFromDB) {
        for (ProductSubCategory productSubCategory : inputProductSubCategoryList) {
            if (productSubCategory.getProductSubCategoryName() == null || productSubCategory.getProductSubCategoryName().equals("")) {
                return defaultExceptionHandler.handleBadRequestException(new BadRequestException("ProductSubCategory name" + productSubCategory.getProductSubCategoryName() + " is mandatory or cannot be null "));
            }
            if (productSubCategory.getProductType() == null || productSubCategory.getProductType().equals("")) {
                return defaultExceptionHandler.handleBadRequestException(new BadRequestException("ProductSubCategory type" + productSubCategory.getProductType() + " is mandatory or cannot be null "));
            }
            if (listFromDB != null && listFromDB.contains(productSubCategory))
                return defaultExceptionHandler.handleBadRequestException(new BadRequestException("ProductSubCategory name " + productSubCategory.getProductSubCategoryName() + " already assigned to this category "));
        }
        return null;
    }

    private ResponseEntity<Object> validateProductSubCategory(ProductSubCategory inputProductSubCategory, List<ProductSubCategory> productSubCategoryListFromDB) {
        if (inputProductSubCategory.getId() == null ||
                inputProductSubCategory.getId() == 0 ||
                inputProductSubCategory.getProductSubCategoryName() == null ||
                inputProductSubCategory.getProductSubCategoryName().equals("")) {
            return defaultExceptionHandler.handleBadRequestException(new BadRequestException("ProductSubCategory Json is not valid"));
        }
        if (productSubCategoryListFromDB != null) {
            boolean prodCategoryFound = false;
            for (ProductSubCategory productSubCategory : productSubCategoryListFromDB) {
                if (productSubCategory.getId() == inputProductSubCategory.getId()) {
                    prodCategoryFound = true;
                    if (!productSubCategory.getProductSubCategoryName().equals(inputProductSubCategory.getProductSubCategoryName())) {
                        return defaultExceptionHandler.handleBadRequestException(new BadRequestException("ProductSubCategory name does not match with the specified Id"));
                    }
                }
            }
            if (!prodCategoryFound) {
                return defaultExceptionHandler.handleBadRequestException(new BadRequestException("ProductSubCategory does not exist in the backend for this category"));
            }

        }
        logger.info("Finally set the isNew to false for Product category");
        inputProductSubCategory.setNewRecord(false);
        return null;
    }

    private ResponseEntity<Object> validateProductList(List<Product> inputProductList, List<Product> productListFromDB) {
        for (Product inputProduct : inputProductList) {
            if (inputProduct.getName() == null || inputProduct.getName().equals("") || inputProduct.getBrand() == null || inputProduct.getBrand().equals("") || inputProduct.getTitle() == null || inputProduct.getTitle().equals("")) {
                return defaultExceptionHandler.handleBadRequestException(new BadRequestException("Product Json not Valid. Product should have name, brand and title"));
            }
            if (productListFromDB != null && productListFromDB.contains(inputProduct)) {
                return defaultExceptionHandler.handleBadRequestException(new BadRequestException("Product already exists in the backend for this product sub category"));
            }
        }
        return null;
    }


    public Category getProductCategoryById(Integer categoryId, Integer id) {
        logger.info("Get Category Object from the database");
        logger.info("Category ID = {}, Product Sub Category Id = {}", categoryId, id);
        Category category = categoryRepository.findById(categoryId).orElse(null);
        if (category == null) {
            return null;
        }
        logger.info("Getting list of product sub-categorys");
        List<ProductSubCategory> listOfProdCategory = category.getProductSubCategorys();

        List<ProductSubCategory> selectProductCatList = new ArrayList<>();
        listOfProdCategory.stream().forEach((productSubCategory) -> {
            logger.info("Product Sub Category Id " + productSubCategory.getId());
            if (productSubCategory.getId() == id) {
                selectProductCatList.add(productSubCategory);
            }
        });

        category.setProductSubCategorys(selectProductCatList);
        return category;
    }
}
