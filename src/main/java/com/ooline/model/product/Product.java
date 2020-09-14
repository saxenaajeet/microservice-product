package com.ooline.model.product;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonView;
import com.ooline.model.views.CategoryView;
import org.springframework.data.annotation.*;
import org.springframework.data.domain.Persistable;
import org.springframework.data.mongodb.core.index.IndexDirection;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Date;
import java.util.Objects;

@Document(collection = "products")
public class Product implements Persistable<Integer> {
    @Transient
    public static final String SEQUENCE_NAME = "products_sequence";
    @Id
    @JsonView(CategoryView.ProductsWithCategory.class)
    private Integer id;
    @JsonView(CategoryView.ProductsWithCategory.class)
    private String name;
    @JsonView(CategoryView.ProductsWithCategory.class)
    private Double maxRetailPrice;
    @JsonView(CategoryView.ProductsWithCategory.class)
    private String brand;
    @JsonView(CategoryView.ProductsWithCategory.class)
    private String title;
    @JsonView(CategoryView.ProductsWithCategory.class)
    private String description;
    @JsonView(CategoryView.ProductsWithCategory.class)
    private String producedIn;
    @JsonView(CategoryView.ProductsWithCategory.class)
    private String length;
    @JsonView(CategoryView.ProductsWithCategory.class)
    private String width;
    @JsonView(CategoryView.ProductsWithCategory.class)
    private String height;
    @JsonView(CategoryView.ProductsWithCategory.class)
    private String productCode;
    @JsonView(CategoryView.ProductsWithCategory.class)
    private String batchCode;
    @JsonView(CategoryView.ProductsWithCategory.class)
    private Double offerPrice;
    @JsonView(CategoryView.ProductsWithCategory.class)
    private String productRating;
    @JsonView(CategoryView.ProductsWithCategory.class)
    private String productImage;
    private boolean isActive;
    @JsonIgnore
    private String productKeywords;
    @LastModifiedDate
    @JsonIgnore
    private Date lastModifiedDate;
    @Indexed(direction = IndexDirection.ASCENDING)
    @CreatedDate
    @JsonIgnore
    private Date createdDate;
    @CreatedBy
    @JsonIgnore
    private Integer createdBy;
    @LastModifiedBy
    @JsonIgnore
    private Integer lastModifiedBy;
    @Transient
    private boolean isNewRecord;

    public Product() {
        super();
    }

    public Product(Integer id, String name, Double maxRetailPrice, String brand, String title, String description, String producedIn, String length, String width, String height, String productCode, String batchCode, Double offerPrice, String productRating, String productImage, boolean isActive, String productKeywords) {
        this.id = id;
        this.name = name;
        this.maxRetailPrice = maxRetailPrice;
        this.brand = brand;
        this.title = title;
        this.description = description;
        this.producedIn = producedIn;
        this.length = length;
        this.width = width;
        this.height = height;
        this.productCode = productCode;
        this.batchCode = batchCode;
        this.offerPrice = offerPrice;
        this.productRating = productRating;
        this.productImage = productImage;
        this.isActive = isActive;
        this.productKeywords = productKeywords;
    }

    public Integer getId() {
        return id;
    }

    @Override
    public boolean isNew() {
        return isNewRecord;
    }

    public void setNewRecord(boolean newRecord) {
        isNewRecord = newRecord;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Double getMaxRetailPrice() {
        return maxRetailPrice;
    }

    public void setMaxRetailPrice(Double maxRetailPrice) {
        this.maxRetailPrice = maxRetailPrice;
    }

    public String getBrand() {
        return brand;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getProducedIn() {
        return producedIn;
    }

    public void setProducedIn(String producedIn) {
        this.producedIn = producedIn;
    }

    public String getLength() {
        return length;
    }

    public void setLength(String length) {
        this.length = length;
    }

    public String getWidth() {
        return width;
    }

    public void setWidth(String width) {
        this.width = width;
    }

    public String getHeight() {
        return height;
    }

    public void setHeight(String height) {
        this.height = height;
    }

    public String getProductCode() {
        return productCode;
    }

    public void setProductCode(String productCode) {
        this.productCode = productCode;
    }

    public String getBatchCode() {
        return batchCode;
    }

    public void setBatchCode(String batchCode) {
        this.batchCode = batchCode;
    }

    public Double getOfferPrice() {
        return offerPrice;
    }

    public void setOfferPrice(Double offerPrice) {
        this.offerPrice = offerPrice;
    }

    public String getProductRating() {
        return productRating;
    }

    public void setProductRating(String productRating) {
        this.productRating = productRating;
    }

    public String getProductImage() {
        return productImage;
    }

    public void setProductImage(String productImage) {
        this.productImage = productImage;
    }

    public boolean isActive() {
        return isActive;
    }

    public void setActive(boolean active) {
        isActive = active;
    }

    public String getProductKeywords() {
        return productKeywords;
    }

    public void setProductKeywords(String productKeywords) {
        this.productKeywords = productKeywords;
    }

    public Date getLastModifiedDate() {
        return lastModifiedDate;
    }

    public void setLastModifiedDate(Date lastModifiedDate) {
        this.lastModifiedDate = lastModifiedDate;
    }

    public Date getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(Date createdDate) {
        this.createdDate = createdDate;
    }

    public Integer getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(Integer createdBy) {
        this.createdBy = createdBy;
    }

    public Integer getLastModifiedBy() {
        return lastModifiedBy;
    }

    public void setLastModifiedBy(Integer lastModifiedBy) {
        this.lastModifiedBy = lastModifiedBy;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Product product = (Product) o;
        return name.equals(product.name) &&
                brand.equals(product.brand) &&
                title.equals(product.title);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, brand, title);
    }
}
