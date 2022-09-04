package br.com.alexmdo.controleorcamentofamiliar.repository;

import br.com.alexmdo.controleorcamentofamiliar.model.Receita;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;

@Repository
public interface ReceitaRepository extends JpaRepository<Receita, Long> {

    @Query(value = "SELECT r from Receita r WHERE year(r.data) = :year AND month(r.data) = :month AND r.descricao = :description")
    List<Receita> findByYearMonthAndDescription(Integer year, Integer month, String description);

    List<Receita> findByDescricaoContaining(String descricao);

    @Query(value = "SELECT r FROM Receita r WHERE year(r.data) = :year AND month(r.data) = :month")
    List<Receita> findByYearAndMonth(Integer year, Integer month);

    @Query(value = "SELECT sum(r.valor) FROM Receita r WHERE year(r.data) = :year AND month(r.data) = :month")
    BigDecimal getSumIncomesByYearAndMonth(Integer year, Integer month);

}
