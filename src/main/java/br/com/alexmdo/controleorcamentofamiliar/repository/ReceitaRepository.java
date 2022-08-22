package br.com.alexmdo.controleorcamentofamiliar.repository;

import br.com.alexmdo.controleorcamentofamiliar.model.Receita;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ReceitaRepository extends JpaRepository<Receita, Long> {

}
