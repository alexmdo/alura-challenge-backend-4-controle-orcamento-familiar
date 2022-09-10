package br.com.alexmdo.controleorcamentofamiliar.controller;

import br.com.alexmdo.controleorcamentofamiliar.model.*;
import br.com.alexmdo.controleorcamentofamiliar.repository.CategoryRepository;
import br.com.alexmdo.controleorcamentofamiliar.repository.ExpenseRepository;
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
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
class ExpenseControllerTest {

    @SuppressWarnings("SpringJavaInjectionPointsAutowiringInspection")
    @Autowired
    MockMvc mockMvc;

    @Autowired
    ExpenseRepository expenseRepository;

    @Autowired
    CategoryRepository categoryRepository;

    @BeforeEach
    void setUp() {
        expenseRepository.deleteAll();
        categoryRepository.deleteAll();

        Category categoryAlimentacao = categoryRepository.save(new Category(new CategoryId("Alimentação", CategoryType.EXPENSE)));
        Category categorySaude = categoryRepository.save(new Category(new CategoryId("Saúde", CategoryType.EXPENSE)));
        Category categoryMoradia = categoryRepository.save(new Category(new CategoryId("Moradia", CategoryType.EXPENSE)));
        Category categoryTransporte = categoryRepository.save(new Category(new CategoryId("Transporte", CategoryType.EXPENSE)));
        Category categoryOutras = categoryRepository.save(new Category(new CategoryId("Outras", CategoryType.EXPENSE)));

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
    void givenSave_whenProvidedAValidRequest_thenItWhouldReturn201() throws Exception {
        URI uri = new URI("/expenses");
        String json = """
                {
                  "description": "Premio 4",
                  "amount": 10000,
                  "date": "2021-08-21",
                  "category": "Alimentação"
                }""";

        mockMvc.perform(MockMvcRequestBuilders
                        .post(uri)
                        .content(json)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andExpect(content().json("""
                        {
                          "description": "Premio 4",
                          "amount": 10000,
                          "date": "2021-08-21",
                          "category": "Alimentação"
                        }"""));
    }

    @Test
    void givenSave_whenDuplicateExpenseIsFound_thenItWhouldReturn400() throws Exception {
        URI uri = new URI("/expenses");
        String json = """
                {
                  "description": "Premio Duplicado",
                  "amount": 10000,
                  "date": "2021-08-21",
                  "category": "Alimentação"
                }""";

        mockMvc.perform(MockMvcRequestBuilders
                        .post(uri)
                        .content(json)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                        .andExpect(content().json("""
                                {
                                  "description": "Premio Duplicado",
                                  "amount": 10000,
                                  "date": "2021-08-21",
                                  "category": "Alimentação"
                                }"""));
        mockMvc.perform(MockMvcRequestBuilders
                        .post(uri)
                        .content(json)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(content().json("""
                        [
                          {
                             "field": "description",
                             "error": "Duplicated expense in the same month"
                          }
                        ]"""));
    }

    @Test
    void givenSave_whenExpenseHasNoCategory_thenItWhouldAssignADefaultOfOutrasAndReturn201() throws Exception {
        URI uri = new URI("/expenses");
        String json = """
                {
                  "description": "Premio Sem Categoria",
                  "amount": 10000,
                  "date": "2021-08-21"
                }""";

        mockMvc.perform(MockMvcRequestBuilders
                        .post(uri)
                        .content(json)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andExpect(content().json("""
                        {
                          "description": "Premio Sem Categoria",
                          "amount": 10000,
                          "date": "2021-08-21",
                          "category": "Outras"
                        }"""));
    }

    @Test
    void givenFindByDescriptionOrAll_whenExpenseIsFound_thenItShouldReturnOkAndAValidArrayResponse() throws Exception {
        URI uri = new URI("/expenses?descricao=MORA");

        mockMvc.perform(MockMvcRequestBuilders
                        .get(uri)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().json("""
                        [
                          	{
                          		"description": "DESPESA MORADIA 5",
                          		"amount": 5000.00,
                          		"date": "2021-08-20",
                          		"category": "Moradia"
                          	},
                          	{
                          		"description": "DESPESA MORADIA 6",
                          		"amount": 6000.00,
                          		"date": "2021-08-25",
                          		"category": "Moradia"
                          	}
                          ]"""));
    }

    @Test
    void givenFindByDescriptionOrAll_whenExpenseIsNotFound_thenItShouldReturnOkAndEmptyArrayResponse() throws Exception {
        URI uri = new URI("/expenses?descricao=XPTO");

        mockMvc.perform(MockMvcRequestBuilders
                        .get(uri)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().json("[]"));
    }

    @Test
    void givenFindByDescriptionOrAll_whenDescricaoRequestParamIsBlank_thenItShouldReturnOkAndAValidArrayResponse() throws Exception {
        URI uri = new URI("/expenses?descricao=");

        mockMvc.perform(MockMvcRequestBuilders
                        .get(uri)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().json("""
                        [
                            {
                              "description": "DESPESA ALIMENTAÇÃO 1",
                              "amount": 1000.0,
                              "date": "2021-08-01",
                              "category": "Alimentação"
                            },
                            {
                              "description": "DESPESA ALIMENTAÇÃO 2",
                              "amount": 2000.0,
                              "date": "2021-08-05",
                              "category": "Alimentação"
                            },
                            {
                              "description": "DESPESA SAÚDE 3",
                              "amount": 3000.0,
                              "date": "2021-08-10",
                              "category": "Saúde"
                            },
                            {
                              "description": "DESPESA SAÚDE 4",
                              "amount": 4000.0,
                              "date": "2021-08-15",
                              "category": "Saúde"
                            },
                            {
                              "description": "DESPESA MORADIA 5",
                              "amount": 5000.0,
                              "date": "2021-08-20",
                              "category": "Moradia"
                            },
                            {
                              "description": "DESPESA MORADIA 6",
                              "amount": 6000.0,
                              "date": "2021-08-25",
                              "category": "Moradia"
                            },
                            {
                              "description": "DESPESA TRANSPORTE 7",
                              "amount": 7000.0,
                              "date": "2021-08-30",
                              "category": "Transporte"
                            },
                            {
                              "description": "DESPESA TRANSPORTE 8",
                              "amount": 8000.0,
                              "date": "2021-09-01",
                              "category": "Transporte"
                            }
                          ]
                        """));
    }

    @Test
    void givenGetDetail_whenExpenseIsFound_thenItShouldReturnOkAndValidResponse() throws Exception {
        Expense expense = expenseRepository.save(new Expense(null, "DESPESA TO BE FOUND", new BigDecimal("2000.00"), LocalDate.of(2022, 2, 15), new Category(new CategoryId("Outras", CategoryType.EXPENSE))));

        URI uri = new URI("/expenses/" + expense.getId());

        mockMvc.perform(MockMvcRequestBuilders
                        .get(uri)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().json("""
                        {
                         	"description": "DESPESA TO BE FOUND",
                         	"amount": 2000.00,
                         	"date": "2022-02-15",
                         	"category": "Outras"
                         }"""));
    }

    @Test
    void givenGetDetail_whenExpenseIsNotFound_thenItShouldReturnNotFoundAndNoResponse() throws Exception {
        URI uri = new URI("/expenses/99");

        mockMvc.perform(MockMvcRequestBuilders
                        .get(uri)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(content().string(""));
    }

    @Test
    void givenFindByYearAndMonth_whenExpenseIsFound_thenItShouldReturnOkAndAValidArrayResponse() throws Exception {
        URI uri = new URI("/expenses/2021/8");

        mockMvc.perform(MockMvcRequestBuilders
                        .get(uri)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().json("""
                        [
                          {
                            "description": "DESPESA ALIMENTAÇÃO 1",
                            "amount": 1000.0,
                            "date": "2021-08-01",
                            "category": "Alimentação"
                          },
                          {
                            "description": "DESPESA ALIMENTAÇÃO 2",
                            "amount": 2000.0,
                            "date": "2021-08-05",
                            "category": "Alimentação"
                          },
                          {
                            "description": "DESPESA SAÚDE 3",
                            "amount": 3000.0,
                            "date": "2021-08-10",
                            "category": "Saúde"
                          },
                          {
                            "description": "DESPESA SAÚDE 4",
                            "amount": 4000.0,
                            "date": "2021-08-15",
                            "category": "Saúde"
                          },
                          {
                            "description": "DESPESA MORADIA 5",
                            "amount": 5000.0,
                            "date": "2021-08-20",
                            "category": "Moradia"
                          },
                          {
                            "description": "DESPESA MORADIA 6",
                            "amount": 6000.0,
                            "date": "2021-08-25",
                            "category": "Moradia"
                          },
                          {
                            "description": "DESPESA TRANSPORTE 7",
                            "amount": 7000.0,
                            "date": "2021-08-30",
                            "category": "Transporte"
                          }
                        ]
                        """));
    }

    @Test
    void givenFindByYearAndMonth_whenExpenseIsNotFound_thenItShouldReturnOkAndNoResponse() throws Exception {
        URI uri = new URI("/expenses/2021/12");

        mockMvc.perform(MockMvcRequestBuilders
                        .get(uri)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().json("[]"));
    }

    @Test
    void givenUpdate_whenExpenseIsFound_thenItShouldReturnOkAndAValidResponse() throws Exception {
        CategoryId food = new CategoryId("Alimentação", CategoryType.EXPENSE);
        Category category = new Category(food);
        Expense expenseToDelete = new Expense(null, "TO DELETE", BigDecimal.TEN, LocalDate.now(), category);
        expenseToDelete = expenseRepository.save(expenseToDelete);

        URI uri = new URI("/expenses/" + expenseToDelete.getId());

        mockMvc.perform(MockMvcRequestBuilders
                        .put(uri)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                            {
                              "description": "Premio UPDATE",
                              "amount": 10000,
                              "date": "2021-08-21"
                            }
                        """))
                .andExpect(status().isOk())
                .andExpect(content().json("""
                        {
                         	"description": "Premio UPDATE",
                            "amount": 10000,
                            "date": "2021-08-21",
                         	"category": "Alimentação"
                         }"""));

        expenseRepository.deleteById(expenseToDelete.getId());
    }

    @Test
    void givenUpdate_whenExpenseIsFoundAndDuplicate_thenItShouldReturnBadRequestAndAValidErrorResponse() throws Exception {
        CategoryId food = new CategoryId("Alimentação", CategoryType.EXPENSE);
        Category category = new Category(food);
        Expense expenseToDelete = new Expense(null, "TO DELETE", BigDecimal.TEN, LocalDate.of(2021, 8, 21), category);
        expenseToDelete = expenseRepository.save(expenseToDelete);

        URI uri = new URI("/expenses/" + expenseToDelete.getId());

        mockMvc.perform(MockMvcRequestBuilders
                        .put(uri)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                            {
                              "description": "TO DELETE",
                              "amount": 10000,
                              "date": "2021-08-21"
                            }
                        """))
                .andExpect(status().isBadRequest())
                .andExpect(content().json("""
                        [
                            {
                                "field": "description",
                                "error": "Duplicated expense in the same month"
                            }
                        ]"""));

        expenseRepository.deleteById(expenseToDelete.getId());
    }

    @Test
    void givenUpdate_whenExpenseIsNotFoundAndDuplicate_thenItShouldReturnNotFoundAndAEmptyResponse() throws Exception {
        URI uri = new URI("/expenses/-1");

        mockMvc.perform(MockMvcRequestBuilders
                        .put(uri)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                            {
                              "description": "TO DELETE",
                              "amount": 10000,
                              "date": "2021-08-21"
                            }
                        """))
                .andExpect(status().isNotFound())
                .andExpect(content().string(""));
    }

    @Test
    void givenDelete_whenExpenseIsFound_thenItShouldReturnOkAndEmptyResponse() throws Exception {
        CategoryId food = new CategoryId("Alimentação", CategoryType.EXPENSE);
        Category category = new Category(food);
        Expense expenseToDelete = new Expense(null, "TO DELETE", BigDecimal.TEN, LocalDate.now(), category);
        expenseToDelete = expenseRepository.save(expenseToDelete);

        URI uri = new URI("/expenses/" + expenseToDelete.getId());

        mockMvc.perform(MockMvcRequestBuilders
                        .delete(uri)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().string(""));

        Optional<Expense> optionalDespesa = expenseRepository.findById(expenseToDelete.getId());
        assertFalse(optionalDespesa.isPresent());
    }

    @Test
    void givenDelete_whenExpenseIsNotFound_thenItShouldReturnNotFoundAndEmptyResponse() throws Exception {
        URI uri = new URI("/expenses/-1");

        mockMvc.perform(MockMvcRequestBuilders
                        .delete(uri)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(content().string(""));
    }
}