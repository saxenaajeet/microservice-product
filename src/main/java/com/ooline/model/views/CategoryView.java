package com.ooline.model.views;

public class CategoryView {
    public interface MinimalCategory {
    }

    public interface CategoryWithSubCategory extends MinimalCategory {
    }

    public interface ProductsWithCategory extends CategoryWithSubCategory {
    }
}
