package br.com.alexmdo.controleorcamentofamiliar.repository;

import br.com.alexmdo.controleorcamentofamiliar.model.Expense;
import br.com.alexmdo.controleorcamentofamiliar.model.vo.ExpenseByCategoryVO;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;

@Repository
public interface ExpenseRepository extends JpaRepository<Expense, Long> {

    @Query(value = "SELECT d from Expense d WHERE year(d.date) = :year AND month(d.date) = :month AND d.description = :description")
    List<Expense> findByYearMonthAndDescription(Integer year, Integer month, String description);

    List<Expense> findByDescriptionContaining(String descricao);

    @Query(value = "SELECT d FROM Expense d WHERE year(d.date) = :year AND month(d.date) = :month")
    List<Expense> findByYearAndMonth(Integer year, Integer month);

    @Query(value = "SELECT sum(d.amount) FROM Expense d WHERE year(d.date) = :year AND month(d.date) = :month")
    BigDecimal getSumExpensesByYearAndMonth(Integer year, Integer month);

    @Query(value = "SELECT new br.com.alexmdo.controleorcamentofamiliar.model.vo.ExpenseByCategoryVO(d.category.id.description, sum(d.amount)) FROM Expense d WHERE year(d.date) = :year AND month(d.date) = :month GROUP BY d.category.id.description")
    List<ExpenseByCategoryVO> getTotalExpenseByCategory(Integer year, Integer month);

}
