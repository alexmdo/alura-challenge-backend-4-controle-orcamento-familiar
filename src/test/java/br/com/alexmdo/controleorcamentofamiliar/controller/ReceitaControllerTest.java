package br.com.alexmdo.controleorcamentofamiliar.controller;

import br.com.alexmdo.controleorcamentofamiliar.model.*;
import br.com.alexmdo.controleorcamentofamiliar.repository.ReceitaRepository;
import org.junit.jupiter.api.BeforeAll;
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

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
class ReceitaControllerTest {

    @SuppressWarnings("SpringJavaInjectionPointsAutowiringInspection")
    @Autowired
    MockMvc mockMvc;

    @Autowired
    ReceitaRepository receitaRepository;

    @BeforeEach
    void setUp() {
        receitaRepository.deleteAll();

        receitaRepository.save(new Receita(null, "RECEITA 1", new BigDecimal("1000.00"), LocalDate.of(2021, 8, 15)));
        receitaRepository.save(new Receita(null, "RECEITA 2", new BigDecimal("2000.00"), LocalDate.of(2021, 8, 20)));
        receitaRepository.save(new Receita(null, "RECEITA 3", new BigDecimal("3000.00"), LocalDate.of(2021, 9, 15)));
        receitaRepository.save(new Receita(null, "RECEITA 4", new BigDecimal("4000.00"), LocalDate.of(2021, 9, 20)));
        receitaRepository.save(new Receita(null, "RECEITA 5", new BigDecimal("5000.00"), LocalDate.of(2021, 10, 15)));
        receitaRepository.save(new Receita(null, "RECEITA 6", new BigDecimal("6000.00"), LocalDate.of(2021, 10, 20)));
    }

    @Test
    void givenSave_whenProvidedAValidRequest_thenItWhouldReturnCreatedAndAValidResponse() throws Exception {
        URI uri = new URI("/receitas");
        String json = """
                {
                  "descricao": "Premio 4",
                  "valor": 10000,
                  "data": "2021-08-21"
                }""";

        mockMvc.perform(MockMvcRequestBuilders
                        .post(uri)
                        .content(json)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andExpect(content().json("""
                        {
                          "descricao": "Premio 4",
                          "valor": 10000,
                          "data": "2021-08-21"
                        }"""));
    }

    @Test
    void givenSave_whenDuplicateExpenseIsFound_thenItWhouldReturn400AndAValidErrorResponse() throws Exception {
        URI uri = new URI("/receitas");
        String json = """
                {
                  "descricao": "Premio Duplicado",
                  "valor": 10000,
                  "data": "2021-08-21"
                }""";

        mockMvc.perform(MockMvcRequestBuilders
                        .post(uri)
                        .content(json)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andExpect(content().json("""
                                {
                                  "descricao": "Premio Duplicado",
                                  "valor": 10000,
                                  "data": "2021-08-21"
                                }"""));
        mockMvc.perform(MockMvcRequestBuilders
                        .post(uri)
                        .content(json)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(content().json("""
                        [
                          {
                             "field": "descricao",
                             "error": "Receita duplicada no mesmo mês"
                          }
                        ]"""));
    }

    @Test
    void givenFindByDescriptionOrAll_whenExpenseIsFound_thenItShouldReturnOkAndAValidArrayResponse() throws Exception {
        URI uri = new URI("/receitas?descricao=RECEITA%203");

        mockMvc.perform(MockMvcRequestBuilders
                        .get(uri)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().json("""
                        [
                        	{
                        		"descricao": "RECEITA 3",
                        		"valor": 3000.00,
                        		"data": "2021-09-15"
                        	}
                        ]
                        """));
    }

    @Test
    void givenFindByDescriptionOrAll_whenExpenseIsNotFound_thenItShouldReturnOkAndEmptyArrayResponse() throws Exception {
        URI uri = new URI("/receitas?descricao=XPTO");

        mockMvc.perform(MockMvcRequestBuilders
                        .get(uri)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().json("[]"));
    }

    @Test
    void givenFindByDescriptionOrAll_whenDescricaoRequestParamIsBlank_thenItShouldReturnOkAndAValidArrayResponse() throws Exception {
        URI uri = new URI("/receitas?descricao=");

        mockMvc.perform(MockMvcRequestBuilders
                        .get(uri)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().json("""
                        [
                        	{
                        		"descricao": "RECEITA 1",
                        		"valor": 1000.00,
                        		"data": "2021-08-15"
                        	},
                        	{
                        		"descricao": "RECEITA 2",
                        		"valor": 2000.00,
                        		"data": "2021-08-20"
                        	},
                        	{
                        		"descricao": "RECEITA 3",
                        		"valor": 3000.00,
                        		"data": "2021-09-15"
                        	},
                        	{
                        		"descricao": "RECEITA 4",
                        		"valor": 4000.00,
                        		"data": "2021-09-20"
                        	},
                        	{
                        		"descricao": "RECEITA 5",
                        		"valor": 5000.00,
                        		"data": "2021-10-15"
                        	},
                        	{
                        		"descricao": "RECEITA 6",
                        		"valor": 6000.00,
                        		"data": "2021-10-20"
                        	}
                        ]
                        """));
    }

    @Test
    void givenGetDetail_whenExpenseIsFound_thenItShouldReturnOkAndValidResponse() throws Exception {
        Receita receita = receitaRepository.save(new Receita(null, "ALUGUEL COTIA", BigDecimal.valueOf(2000.00), LocalDate.of(2021, 2, 15)));

        URI uri = new URI("/receitas/" + receita.getId());

        mockMvc.perform(MockMvcRequestBuilders
                        .get(uri)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().json("{\n" +
                                          " 	\"id\": " + receita.getId() + ",\n" +
                                          " 	\"descricao\": \"ALUGUEL COTIA\",\n" +
                                          " 	\"valor\": 2000.00,\n" +
                                          " 	\"data\": \"2021-02-15\"\n" +
                                          " }"));
    }

    @Test
    void givenGetDetail_whenExpenseIsNotFound_thenItShouldReturnNotFoundAndNoResponse() throws Exception {
        URI uri = new URI("/receitas/-1");

        mockMvc.perform(MockMvcRequestBuilders
                        .get(uri)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(content().string(""));
    }

    @Test
    void givenFindByYearAndMonth_whenExpenseIsFound_thenItShouldReturnOkAndAValidArrayResponse() throws Exception {
        URI uri = new URI("/receitas/2021/8");

        mockMvc.perform(MockMvcRequestBuilders
                        .get(uri)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().json("""
                        [
                           	{
                           		"descricao": "RECEITA 1",
                           		"valor": 1000.00,
                           		"data": "2021-08-15"
                           	},
                           	{
                           		"descricao": "RECEITA 2",
                           		"valor": 2000.00,
                           		"data": "2021-08-20"
                           	}
                           ]"""));
    }

    @Test
    void givenFindByYearAndMonth_whenExpenseIsNotFound_thenItShouldReturnOkAndNoResponse() throws Exception {
        URI uri = new URI("/despesas/2021/-1");

        mockMvc.perform(MockMvcRequestBuilders
                        .get(uri)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().json("[]"));
    }

    @Test
    void givenUpdate_whenExpenseIsFound_thenItShouldReturnOkAndAValidResponse() throws Exception {
        Receita receita = receitaRepository.save(new Receita(null, "RECEITA TO DELETE", new BigDecimal("1000.00"), LocalDate.of(2021, 8, 15)));

        URI uri = new URI("/receitas/" + receita.getId());

        mockMvc.perform(MockMvcRequestBuilders
                        .put(uri)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                            {
                              "descricao": "Premio UPDATE",
                              "valor": 10000,
                              "data": "2021-08-21"
                            }
                        """))
                .andExpect(status().isOk())
                .andExpect(content().json("""
                        {
                         	"descricao": "Premio UPDATE",
                            "valor": 10000,
                            "data": "2021-08-21"
                         }"""));
    }

    @Test
    void givenUpdate_whenExpenseIsFoundAndDuplicate_thenItShouldReturnBadRequestAndAValidErrorResponse() throws Exception {
        Receita receita = receitaRepository.save(new Receita(null, "RECEITA DUPLICATED", new BigDecimal("1000.00"), LocalDate.of(2021, 8, 15)));

        URI uri = new URI("/receitas/" + receita.getId());

        mockMvc.perform(MockMvcRequestBuilders
                        .put(uri)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                            {
                              "descricao": "RECEITA DUPLICATED",
                              "valor": 10000,
                              "data": "2021-08-15"
                            }
                        """))
                .andExpect(status().isBadRequest())
                .andExpect(content().json("""
                        [
                            {
                                "field": "descricao",
                                "error": "Receita duplicada no mesmo mês"
                            }
                        ]"""));
    }

    @Test
    void givenUpdate_whenExpenseIsNotFoundAndDuplicate_thenItShouldReturnNotFoundAndAEmptyResponse() throws Exception {
        URI uri = new URI("/receitas/-1");

        mockMvc.perform(MockMvcRequestBuilders
                        .put(uri)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                            {
                              "descricao": "NOT FOUND",
                              "valor": 10000,
                              "data": "2021-08-21"
                            }
                        """))
                .andExpect(status().isNotFound())
                .andExpect(content().string(""));
    }

    @Test
    void delete() {
    }
}