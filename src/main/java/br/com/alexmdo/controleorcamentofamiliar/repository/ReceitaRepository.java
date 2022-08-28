package br.com.alexmdo.controleorcamentofamiliar.repository;

import br.com.alexmdo.controleorcamentofamiliar.model.Receita;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ReceitaRepository extends JpaRepository<Receita, Long> {

    List<Receita> findByDescricao(String descricao);

    List<Receita> findByDescricaoContaining(String descricao);

    @Query(value = "SELECT * FROM receitas r WHERE year(r.data) = :year AND month(r.data) = :month ", nativeQuery = true)
    List<Receita> findByYearAndMonth(Integer year, Integer month);

}
