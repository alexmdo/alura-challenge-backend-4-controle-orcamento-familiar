package br.com.alexmdo.controleorcamentofamiliar.repository;

import br.com.alexmdo.controleorcamentofamiliar.model.Despesa;
import br.com.alexmdo.controleorcamentofamiliar.model.dto.ExpenseByCategoryDTO;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Repository
public interface DespesaRepository extends JpaRepository<Despesa, Long> {

    @Query(value = "SELECT d from Despesa d WHERE year(d.data) = :year AND month(d.data) = :month AND d.descricao = :description")
    List<Despesa> findByYearMonthAndDescription(Integer year, Integer month, String description);

    List<Despesa> findByDescricaoContaining(String descricao);

    @Query(value = "SELECT d FROM Despesa d WHERE year(d.data) = :year AND month(d.data) = :month")
    List<Despesa> findByYearAndMonth(Integer year, Integer month);

    @Query(value = "SELECT sum(d.valor) FROM Despesa d WHERE year(d.data) = :year AND month(d.data) = :month")
    BigDecimal getSumExpensesByYearAndMonth(Integer year, Integer month);

    @Query(value = "SELECT new br.com.alexmdo.controleorcamentofamiliar.model.dto.ExpenseByCategoryDTO(d.categoria.id.descricao, sum(d.valor)) FROM Despesa d WHERE year(d.data) = :year AND month(d.data) = :month GROUP BY d.categoria.id.descricao")
    List<ExpenseByCategoryDTO> getTotalExpenseByCategory(Integer year, Integer month);

}
