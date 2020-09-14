package com.ooline.repository;

import com.ooline.model.Category;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CategoryRepository extends MongoRepository<Category, Integer> {

    Category findByCategoryName(String categoryName);
}
