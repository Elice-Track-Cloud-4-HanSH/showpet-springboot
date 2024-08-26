package com.elice.showpet.category.repository;

import com.elice.showpet.category.entity.Category;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;



@Transactional
public interface CategoryRepository extends JpaRepository<Category, Long> {
}
