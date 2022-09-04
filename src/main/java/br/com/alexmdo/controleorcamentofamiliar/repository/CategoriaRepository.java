package br.com.alexmdo.controleorcamentofamiliar.repository;

import br.com.alexmdo.controleorcamentofamiliar.model.Categoria;
import br.com.alexmdo.controleorcamentofamiliar.model.CategoriaId;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CategoriaRepository extends JpaRepository<Categoria, CategoriaId> {
}