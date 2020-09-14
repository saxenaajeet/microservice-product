package com.ooline.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonView;
import com.ooline.annotation.CascadeSave;
import com.ooline.model.product.ProductSubCategory;
import com.ooline.model.views.CategoryView;
import org.springframework.data.annotation.*;
import org.springframework.data.domain.Persistable;
import org.springframework.data.mongodb.core.index.IndexDirection;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Date;
import java.util.List;

/**
 * Category class - A common file
 */
@Document(collection = "category")
public class Category implements Persistable<Integer> {
    @Transient
    public static final String SEQUENCE_NAME = "category_sequence";
    @Id
    @JsonView({CategoryView.MinimalCategory.class})
    private Integer id;
    @Indexed(unique = true)
    @JsonView({CategoryView.MinimalCategory.class})
    private String categoryName;
    @JsonView({CategoryView.MinimalCategory.class})
    private String categoryImage;
    @JsonView({CategoryView.MinimalCategory.class})
    private String categoryType;

    @JsonProperty("productsubcategory")
    @JsonView(CategoryView.CategoryWithSubCategory.class)
    @DBRef(lazy = true)
    @CascadeSave()
    private List<ProductSubCategory> productSubCategorys;

    @JsonIgnore
    @LastModifiedDate
    private Date lastModifiedDate;
    @Indexed(direction = IndexDirection.ASCENDING)
    @JsonIgnore
    @CreatedDate
    private Date createdDate;
    @JsonIgnore
    @CreatedBy
    private Integer createdBy;
    @JsonIgnore
    @LastModifiedBy
    private Integer lastModifiedBy;
    @Transient
    private boolean isNewRecord;

    public Category() {

    }

    public Category(@JsonProperty("id") Integer id, @JsonProperty("categoryName") String categoryName, @JsonProperty("categoryImage") String categoryImage, @JsonProperty("categoryType") String categoryType) {
        this.id = id;
        this.categoryName = categoryName;
        this.categoryImage = categoryImage;
        this.categoryType = categoryType;
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

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    public String getCategoryImage() {
        return categoryImage;
    }

    public void setCategoryImage(String categoryImage) {
        this.categoryImage = categoryImage;
    }

    public String getCategoryType() {
        return categoryType;
    }

    public void setCategoryType(String categoryType) {
        this.categoryType = categoryType;
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


    public List<ProductSubCategory> getProductSubCategorys() {
        return productSubCategorys;
    }

    public void setProductSubCategorys(List<ProductSubCategory> productSubCategorys) {
        this.productSubCategorys = productSubCategorys;
    }

    @Override
    public String toString() {
        return String.format(
                "Category[id=%s, name='%s', categoryType='%s']",
                id, categoryName, categoryType);
    }
}
