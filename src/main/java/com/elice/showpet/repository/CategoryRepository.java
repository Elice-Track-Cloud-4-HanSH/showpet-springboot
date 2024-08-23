package com.elice.showpet.repository;

import com.elice.showpet.domain.Category;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;



@Transactional
public interface CategoryRepository extends JpaRepository<Category, Long> {
}
