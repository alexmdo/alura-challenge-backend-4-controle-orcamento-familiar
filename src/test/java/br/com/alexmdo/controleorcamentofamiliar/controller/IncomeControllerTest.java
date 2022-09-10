package br.com.alexmdo.controleorcamentofamiliar.controller;

import br.com.alexmdo.controleorcamentofamiliar.model.Income;
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
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
class IncomeControllerTest {

    @SuppressWarnings("SpringJavaInjectionPointsAutowiringInspection")
    @Autowired
    MockMvc mockMvc;

    @Autowired
    IncomeRepository incomeRepository;

    @BeforeEach
    void setUp() {
        incomeRepository.deleteAll();

        incomeRepository.save(new Income(null, "RECEITA 1", new BigDecimal("1000.00"), LocalDate.of(2021, 8, 15)));
        incomeRepository.save(new Income(null, "RECEITA 2", new BigDecimal("2000.00"), LocalDate.of(2021, 8, 20)));
        incomeRepository.save(new Income(null, "RECEITA 3", new BigDecimal("3000.00"), LocalDate.of(2021, 9, 15)));
        incomeRepository.save(new Income(null, "RECEITA 4", new BigDecimal("4000.00"), LocalDate.of(2021, 9, 20)));
        incomeRepository.save(new Income(null, "RECEITA 5", new BigDecimal("5000.00"), LocalDate.of(2021, 10, 15)));
        incomeRepository.save(new Income(null, "RECEITA 6", new BigDecimal("6000.00"), LocalDate.of(2021, 10, 20)));
    }

    @Test
    void givenSave_whenProvidedAValidRequest_thenItWhouldReturnCreatedAndAValidResponse() throws Exception {
        URI uri = new URI("/incomes");
        String json = """
                {
                  "description": "Premio 4",
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
                          "description": "Premio 4",
                          "amount": 10000,
                          "date": "2021-08-21"
                        }"""));
    }

    @Test
    void givenSave_whenDuplicateIncomeIsFound_thenItWhouldReturn400AndAValidErrorResponse() throws Exception {
        URI uri = new URI("/incomes");
        String json = """
                {
                  "description": "Premio Duplicado",
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
                                  "description": "Premio Duplicado",
                                  "amount": 10000,
                                  "date": "2021-08-21"
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
                             "error": "Duplicated income in the same month"
                          }
                        ]"""));
    }

    @Test
    void givenFindByDescriptionOrAll_whenIncomeIsFound_thenItShouldReturnOkAndAValidArrayResponse() throws Exception {
        URI uri = new URI("/incomes?descricao=RECEITA%203");

        mockMvc.perform(MockMvcRequestBuilders
                        .get(uri)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().json("""
                        [
                        	{
                        		"description": "RECEITA 3",
                        		"amount": 3000.00,
                        		"date": "2021-09-15"
                        	}
                        ]
                        """));
    }

    @Test
    void givenFindByDescriptionOrAll_whenIncomeIsNotFound_thenItShouldReturnOkAndEmptyArrayResponse() throws Exception {
        URI uri = new URI("/incomes?descricao=XPTO");

        mockMvc.perform(MockMvcRequestBuilders
                        .get(uri)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().json("[]"));
    }

    @Test
    void givenFindByDescriptionOrAll_whenDescricaoRequestParamIsBlank_thenItShouldReturnOkAndAValidArrayResponse() throws Exception {
        URI uri = new URI("/incomes?descricao=");

        mockMvc.perform(MockMvcRequestBuilders
                        .get(uri)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().json("""
                        [
                        	{
                        		"description": "RECEITA 1",
                        		"amount": 1000.00,
                        		"date": "2021-08-15"
                        	},
                        	{
                        		"description": "RECEITA 2",
                        		"amount": 2000.00,
                        		"date": "2021-08-20"
                        	},
                        	{
                        		"description": "RECEITA 3",
                        		"amount": 3000.00,
                        		"date": "2021-09-15"
                        	},
                        	{
                        		"description": "RECEITA 4",
                        		"amount": 4000.00,
                        		"date": "2021-09-20"
                        	},
                        	{
                        		"description": "RECEITA 5",
                        		"amount": 5000.00,
                        		"date": "2021-10-15"
                        	},
                        	{
                        		"description": "RECEITA 6",
                        		"amount": 6000.00,
                        		"date": "2021-10-20"
                        	}
                        ]
                        """));
    }

    @Test
    void givenGetDetail_whenIncomeIsFound_thenItShouldReturnOkAndValidResponse() throws Exception {
        Income income = incomeRepository.save(new Income(null, "ALUGUEL COTIA", BigDecimal.valueOf(2000.00), LocalDate.of(2021, 2, 15)));

        URI uri = new URI("/incomes/" + income.getId());

        mockMvc.perform(MockMvcRequestBuilders
                        .get(uri)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().json("{\n" +
                                          " 	\"id\": " + income.getId() + ",\n" +
                                          " 	\"description\": \"ALUGUEL COTIA\",\n" +
                                          " 	\"amount\": 2000.00,\n" +
                                          " 	\"date\": \"2021-02-15\"\n" +
                                          " }"));
    }

    @Test
    void givenGetDetail_whenIncomeIsNotFound_thenItShouldReturnNotFoundAndNoResponse() throws Exception {
        URI uri = new URI("/incomes/-1");

        mockMvc.perform(MockMvcRequestBuilders
                        .get(uri)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(content().string(""));
    }

    @Test
    void givenFindByYearAndMonth_whenIncomeIsFound_thenItShouldReturnOkAndAValidArrayResponse() throws Exception {
        URI uri = new URI("/incomes/2021/8");

        mockMvc.perform(MockMvcRequestBuilders
                        .get(uri)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().json("""
                        [
                           	{
                           		"description": "RECEITA 1",
                           		"amount": 1000.00,
                           		"date": "2021-08-15"
                           	},
                           	{
                           		"description": "RECEITA 2",
                           		"amount": 2000.00,
                           		"date": "2021-08-20"
                           	}
                           ]"""));
    }

    @Test
    void givenFindByYearAndMonth_whenIncomeIsNotFound_thenItShouldReturnOkAndNoResponse() throws Exception {
        URI uri = new URI("/incomes/2021/-1");

        mockMvc.perform(MockMvcRequestBuilders
                        .get(uri)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().json("[]"));
    }

    @Test
    void givenUpdate_whenIncomeIsFound_thenItShouldReturnOkAndAValidResponse() throws Exception {
        Income income = incomeRepository.save(new Income(null, "RECEITA TO DELETE", new BigDecimal("1000.00"), LocalDate.of(2021, 8, 15)));

        URI uri = new URI("/incomes/" + income.getId());

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
                            "date": "2021-08-21"
                         }"""));
    }

    @Test
    void givenUpdate_whenIncomeIsFoundAndDuplicate_thenItShouldReturnBadRequestAndAValidErrorResponse() throws Exception {
        Income income = incomeRepository.save(new Income(null, "RECEITA DUPLICATED", new BigDecimal("1000.00"), LocalDate.of(2021, 8, 15)));

        URI uri = new URI("/incomes/" + income.getId());

        mockMvc.perform(MockMvcRequestBuilders
                        .put(uri)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                            {
                              "description": "RECEITA DUPLICATED",
                              "amount": 10000,
                              "date": "2021-08-15"
                            }
                        """))
                .andExpect(status().isBadRequest())
                .andExpect(content().json("""
                        [{"field":"description","error":"Duplicate income in the same month"}]"""));
    }

    @Test
    void givenUpdate_whenIncomeIsNotFoundAndDuplicate_thenItShouldReturnNotFoundAndAEmptyResponse() throws Exception {
        URI uri = new URI("/incomes/-1");

        mockMvc.perform(MockMvcRequestBuilders
                        .put(uri)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                            {
                              "description": "NOT FOUND",
                              "amount": 10000,
                              "date": "2021-08-21"
                            }
                        """))
                .andExpect(status().isNotFound())
                .andExpect(content().string(""));
    }

    @Test
    void givenDelete_whenIncomeIsFound_thenItShouldReturnOkAndEmptyResponse() throws Exception {
        Income incomeToDelete = incomeRepository.save(new Income(null, "RECEITA 1", new BigDecimal("1000.00"), LocalDate.of(2021, 8, 15)));

        URI uri = new URI("/incomes/" + incomeToDelete.getId());

        mockMvc.perform(MockMvcRequestBuilders
                        .delete(uri)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().string(""));

        Optional<Income> optionalDespesa = incomeRepository.findById(incomeToDelete.getId());
        assertFalse(optionalDespesa.isPresent());
    }

    @Test
    void givenDelete_whenIncomeIsNotFound_thenItShouldReturnNotFoundAndEmptyResponse() throws Exception {
        URI uri = new URI("/incomes/-1");

        mockMvc.perform(MockMvcRequestBuilders
                        .delete(uri)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(content().string(""));
    }
}