package br.com.alexmdo.controleorcamentofamiliar.controller;

import br.com.alexmdo.controleorcamentofamiliar.model.*;
import br.com.alexmdo.controleorcamentofamiliar.repository.CategoriaRepository;
import br.com.alexmdo.controleorcamentofamiliar.repository.DespesaRepository;
import br.com.alexmdo.controleorcamentofamiliar.repository.ReceitaRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.math.BigDecimal;
import java.net.URI;
import java.time.LocalDate;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
class RelatorioControllerTest {

    @Autowired
    ReceitaRepository receitaRepository;
    @Autowired
    DespesaRepository despesaRepository;
    @Autowired
    CategoriaRepository categoriaRepository;

    @SuppressWarnings("SpringJavaInjectionPointsAutowiringInspection")
    @Autowired
    MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        receitaRepository.deleteAll();
        despesaRepository.deleteAll();
        categoriaRepository.deleteAll();

        receitaRepository.save(new Receita(null, "RECEITA 1", new BigDecimal("1000.00"), LocalDate.of(2021, 8, 15)));
        receitaRepository.save(new Receita(null, "RECEITA 2", new BigDecimal("2000.00"), LocalDate.of(2021, 8, 20)));
        receitaRepository.save(new Receita(null, "RECEITA 3", new BigDecimal("3000.00"), LocalDate.of(2021, 9, 15)));
        receitaRepository.save(new Receita(null, "RECEITA 4", new BigDecimal("4000.00"), LocalDate.of(2021, 9, 20)));
        receitaRepository.save(new Receita(null, "RECEITA 5", new BigDecimal("5000.00"), LocalDate.of(2021, 10, 15)));
        receitaRepository.save(new Receita(null, "RECEITA 6", new BigDecimal("6000.00"), LocalDate.of(2021, 10, 20)));

        Categoria categoriaAlimentacao = categoriaRepository.save(new Categoria(new CategoriaId("Alimentação", CategoriaType.DESPESA)));
        Categoria categoriaSaude = categoriaRepository.save(new Categoria(new CategoriaId("Saúde", CategoriaType.DESPESA)));
        Categoria categoriaMoradia = categoriaRepository.save(new Categoria(new CategoriaId("Moradia", CategoriaType.DESPESA)));
        Categoria categoriaTransporte = categoriaRepository.save(new Categoria(new CategoriaId("Transporte", CategoriaType.DESPESA)));

        despesaRepository.save(new Despesa(null, "DESPESA ALIMENTAÇÃO 1", new BigDecimal("1000.00"), LocalDate.of(2021, 8, 1), categoriaAlimentacao));
        despesaRepository.save(new Despesa(null, "DESPESA ALIMENTAÇÃO 2", new BigDecimal("2000.00"), LocalDate.of(2021, 8, 5), categoriaAlimentacao));
        despesaRepository.save(new Despesa(null, "DESPESA SAÚDE 3", new BigDecimal("3000.00"), LocalDate.of(2021, 8, 10), categoriaSaude));
        despesaRepository.save(new Despesa(null, "DESPESA SAÚDE 4", new BigDecimal("4000.00"), LocalDate.of(2021, 8, 15), categoriaSaude));
        despesaRepository.save(new Despesa(null, "DESPESA MORADIA 5", new BigDecimal("5000.00"), LocalDate.of(2021, 8, 20), categoriaMoradia));
        despesaRepository.save(new Despesa(null, "DESPESA MORADIA 6", new BigDecimal("6000.00"), LocalDate.of(2021, 8, 25), categoriaMoradia));
        despesaRepository.save(new Despesa(null, "DESPESA TRANSPORTE 7", new BigDecimal("7000.00"), LocalDate.of(2021, 8, 30), categoriaTransporte));
        despesaRepository.save(new Despesa(null, "DESPESA TRANSPORTE 8", new BigDecimal("8000.00"), LocalDate.of(2021, 9, 1), categoriaTransporte));
    }

    @Test
    void generateSummaryOfTheMonth() throws Exception {
        URI uri = new URI("/resumo/2021/8");

        mockMvc.perform(MockMvcRequestBuilders
                        .get(uri)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().json("""
                        {
                        	"totalIncome": 3000.00,
                        	"totalExpense": 28000.00,
                        	"balance": -25000.00,
                        	"expenseByCategory": [
                        		{
                        			"description": "Alimentação",
                        			"total": 3000.00
                        		},
                        		{
                        			"description": "Saúde",
                        			"total": 7000.00
                        		},
                        		{
                        			"description": "Moradia",
                        			"total": 11000.00
                        		},
                        		{
                        			"description": "Transporte",
                        			"total": 7000.00
                        		}
                        	]
                        }
                        """));
    }
}