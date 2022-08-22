package br.com.alexmdo.controleorcamentofamiliar;

import br.com.alexmdo.controleorcamentofamiliar.model.Despesa;
import br.com.alexmdo.controleorcamentofamiliar.model.Receita;
import br.com.alexmdo.controleorcamentofamiliar.repository.DespesaRepository;
import br.com.alexmdo.controleorcamentofamiliar.repository.ReceitaRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.math.BigDecimal;
import java.time.LocalDate;

@SpringBootApplication
public class AluraChallengeBackendControleOrcamentoFamiliarApplication {

    public static void main(String[] args) {
        SpringApplication.run(AluraChallengeBackendControleOrcamentoFamiliarApplication.class, args);
    }

    /*@Bean
    public CommandLineRunner demo(ReceitaRepository receitaRepository, DespesaRepository despesaRepository) {
        return (args -> {
           receitaRepository.save(new Receita(null, "Salario", BigDecimal.TEN, LocalDate.now()));
           despesaRepository.save(new Despesa(null, "iPhone 12", BigDecimal.valueOf(4000l), LocalDate.now()));
        });
    }*/

}
