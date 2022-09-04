package br.com.alexmdo.controleorcamentofamiliar.repository;

import br.com.alexmdo.controleorcamentofamiliar.model.Despesa;
import br.com.alexmdo.controleorcamentofamiliar.model.dto.ExpenseByCategoryDTO;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;

@Repository
public interface DespesaRepository extends JpaRepository<Despesa, Long> {

    List<Despesa> findByDescricao(String descricao);

    List<Despesa> findByDescricaoContaining(String descricao);

    @Query(value = "SELECT d FROM Despesa d WHERE year(d.data) = :year AND month(d.data) = :month")
    List<Despesa> findByYearAndMonth(Integer year, Integer month);

    @Query(value = "SELECT sum(d.valor) FROM Despesa d WHERE year(d.data) = :year AND month(d.data) = :month")
    BigDecimal getSumExpensesByYearAndMonth(Integer year, Integer month);

    @Query(value = "SELECT new br.com.alexmdo.controleorcamentofamiliar.model.dto.ExpenseByCategoryDTO(d.categoria.id.descricao, sum(d.valor)) FROM Despesa d WHERE year(d.data) = :year AND month(d.data) = :month GROUP BY d.categoria.id.descricao")
    List<ExpenseByCategoryDTO> getTotalExpenseByCategory(Integer year, Integer month);

}
