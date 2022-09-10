package br.com.alexmdo.controleorcamentofamiliar.controller;

import br.com.alexmdo.controleorcamentofamiliar.model.*;
import br.com.alexmdo.controleorcamentofamiliar.repository.CategoryRepository;
import br.com.alexmdo.controleorcamentofamiliar.repository.ExpenseRepository;
import br.com.alexmdo.controleorcamentofamiliar.repository.IncomeRepository;
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
class ReportControllerTest {

    @Autowired
    IncomeRepository incomeRepository;
    @Autowired
    ExpenseRepository expenseRepository;
    @Autowired
    CategoryRepository categoryRepository;

    @SuppressWarnings("SpringJavaInjectionPointsAutowiringInspection")
    @Autowired
    MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        incomeRepository.deleteAll();
        expenseRepository.deleteAll();
        categoryRepository.deleteAll();

        incomeRepository.save(new Income(null, "RECEITA 1", new BigDecimal("1000.00"), LocalDate.of(2021, 8, 15)));
        incomeRepository.save(new Income(null, "RECEITA 2", new BigDecimal("2000.00"), LocalDate.of(2021, 8, 20)));
        incomeRepository.save(new Income(null, "RECEITA 3", new BigDecimal("3000.00"), LocalDate.of(2021, 9, 15)));
        incomeRepository.save(new Income(null, "RECEITA 4", new BigDecimal("4000.00"), LocalDate.of(2021, 9, 20)));
        incomeRepository.save(new Income(null, "RECEITA 5", new BigDecimal("5000.00"), LocalDate.of(2021, 10, 15)));
        incomeRepository.save(new Income(null, "RECEITA 6", new BigDecimal("6000.00"), LocalDate.of(2021, 10, 20)));

        Category categoryAlimentacao = categoryRepository.save(new Category(new CategoryId("Alimentação", CategoryType.EXPENSE)));
        Category categorySaude = categoryRepository.save(new Category(new CategoryId("Saúde", CategoryType.EXPENSE)));
        Category categoryMoradia = categoryRepository.save(new Category(new CategoryId("Moradia", CategoryType.EXPENSE)));
        Category categoryTransporte = categoryRepository.save(new Category(new CategoryId("Transporte", CategoryType.EXPENSE)));

        expenseRepository.save(new Expense(null, "DESPESA ALIMENTAÇÃO 1", new BigDecimal("1000.00"), LocalDate.of(2021, 8, 1), categoryAlimentacao));
        expenseRepository.save(new Expense(null, "DESPESA ALIMENTAÇÃO 2", new BigDecimal("2000.00"), LocalDate.of(2021, 8, 5), categoryAlimentacao));
        expenseRepository.save(new Expense(null, "DESPESA SAÚDE 3", new BigDecimal("3000.00"), LocalDate.of(2021, 8, 10), categorySaude));
        expenseRepository.save(new Expense(null, "DESPESA SAÚDE 4", new BigDecimal("4000.00"), LocalDate.of(2021, 8, 15), categorySaude));
        expenseRepository.save(new Expense(null, "DESPESA MORADIA 5", new BigDecimal("5000.00"), LocalDate.of(2021, 8, 20), categoryMoradia));
        expenseRepository.save(new Expense(null, "DESPESA MORADIA 6", new BigDecimal("6000.00"), LocalDate.of(2021, 8, 25), categoryMoradia));
        expenseRepository.save(new Expense(null, "DESPESA TRANSPORTE 7", new BigDecimal("7000.00"), LocalDate.of(2021, 8, 30), categoryTransporte));
        expenseRepository.save(new Expense(null, "DESPESA TRANSPORTE 8", new BigDecimal("8000.00"), LocalDate.of(2021, 9, 1), categoryTransporte));
    }

    @Test
    void generateSummaryOfTheMonth() throws Exception {
        URI uri = new URI("/summary/2021/8");

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