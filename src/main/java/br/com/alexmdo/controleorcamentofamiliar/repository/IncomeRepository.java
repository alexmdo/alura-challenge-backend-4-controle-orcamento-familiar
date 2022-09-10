package br.com.alexmdo.controleorcamentofamiliar.repository;

import br.com.alexmdo.controleorcamentofamiliar.model.Income;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;

@Repository
public interface IncomeRepository extends JpaRepository<Income, Long> {

    @Query(value = "SELECT r from Income r WHERE year(r.date) = :year AND month(r.date) = :month AND r.description = :description")
    List<Income> findByYearMonthAndDescription(Integer year, Integer month, String description);

    List<Income> findByDescriptionContaining(String descricao);

    @Query(value = "SELECT r FROM Income r WHERE year(r.date) = :year AND month(r.date) = :month")
    List<Income> findByYearAndMonth(Integer year, Integer month);

    @Query(value = "SELECT sum(r.amount) FROM Income r WHERE year(r.date) = :year AND month(r.date) = :month")
    BigDecimal getSumIncomesByYearAndMonth(Integer year, Integer month);

}
