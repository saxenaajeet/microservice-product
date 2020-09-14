package com.ooline.model.product;

import com.fasterxml.jackson.annotation.JsonView;
import com.ooline.annotation.CascadeSave;
import com.ooline.model.views.CategoryView;
import org.springframework.data.annotation.*;
import org.springframework.data.domain.Persistable;
import org.springframework.data.mongodb.core.index.IndexDirection;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Date;
import java.util.List;
import java.util.Objects;

@Document(collection = "productsubcategory")
public class ProductSubCategory implements Persistable<Integer> {
    @Transient
    public static final String SEQUENCE_NAME = "productsubcategory_sequence";
    @JsonView(CategoryView.CategoryWithSubCategory.class)
    @Id
    private Integer id;
    @JsonView(CategoryView.CategoryWithSubCategory.class)
    private String productSubCategoryName;
    @JsonView(CategoryView.CategoryWithSubCategory.class)
    private String productSubCategoryDesc;
    @JsonView(CategoryView.CategoryWithSubCategory.class)
    private String productType;
    @JsonView(CategoryView.ProductsWithCategory.class)
    @CascadeSave
    @DBRef
    private List<Product> products;
    @LastModifiedDate
    private Date lastModifiedDate;
    @Indexed(direction = IndexDirection.ASCENDING)
    @CreatedDate
    private Date createdDate;
    @CreatedBy
    private Integer createdBy;
    @LastModifiedBy
    private Integer lastModifiedBy;
    @Transient
    private boolean isNewRecord;

    public ProductSubCategory() {
        super();
    }

    public ProductSubCategory(Integer id, String productSubCategoryName, String productSubCategoryDesc, String productType, List<Product> products) {
        this.id = id;
        this.productSubCategoryName = productSubCategoryName;
        this.productSubCategoryDesc = productSubCategoryDesc;
        this.productType = productType;
        this.products = products;
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

    public String getProductSubCategoryName() {
        return productSubCategoryName;
    }

    public void setProductSubCategoryName(String productSubCategoryName) {
        this.productSubCategoryName = productSubCategoryName;
    }

    public String getProductSubCategoryDesc() {
        return productSubCategoryDesc;
    }

    public void setProductSubCategoryDesc(String productSubCategoryDesc) {
        this.productSubCategoryDesc = productSubCategoryDesc;
    }

    public String getProductType() {
        return productType;
    }

    public void setProductType(String productType) {
        this.productType = productType;
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

    public List<Product> getProducts() {
        return products;
    }

    public void setProducts(List<Product> products) {
        this.products = products;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ProductSubCategory that = (ProductSubCategory) o;
        return productSubCategoryName.equals(that.productSubCategoryName) &&
                productType.equals(that.productType);
    }

    @Override
    public int hashCode() {
        return Objects.hash(productSubCategoryName, productType);
    }
}
