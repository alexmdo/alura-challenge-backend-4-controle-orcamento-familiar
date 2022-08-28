package br.com.alexmdo.controleorcamentofamiliar.repository;

import br.com.alexmdo.controleorcamentofamiliar.model.Despesa;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DespesaRepository extends JpaRepository<Despesa, Long> {

    List<Despesa> findByDescricao(String descricao);

    List<Despesa> findByDescricaoContaining(String descricao);

    @Query(value = "SELECT d FROM Despesa d WHERE year(d.data) = :year AND month(d.data) = :month")
    List<Despesa> findByYearAndMonth(Integer year, Integer month);
}
