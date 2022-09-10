package br.com.alexmdo.controleorcamentofamiliar.repository;

import br.com.alexmdo.controleorcamentofamiliar.model.Category;
import br.com.alexmdo.controleorcamentofamiliar.model.CategoryId;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CategoryRepository extends JpaRepository<Category, CategoryId> {
}