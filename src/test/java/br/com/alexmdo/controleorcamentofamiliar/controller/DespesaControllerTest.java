package br.com.alexmdo.controleorcamentofamiliar.controller;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.net.URI;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
class DespesaControllerTest {

    @SuppressWarnings("SpringJavaInjectionPointsAutowiringInspection")
    @Autowired
    MockMvc mockMvc;

    @Test
    void givenSave_whenProvidedAValidRequest_thenItWhouldReturn201() throws Exception {
        URI uri = new URI("/despesas");
        String json = """
                {
                  "descricao": "Premio 4",
                  "valor": 10000,
                  "data": "2021-08-21",
                  "categoria": "Alimentação"
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
                          "data": "2021-08-21",
                          "categoria": "Alimentação"
                        }"""));
    }

    @Test
    void givenSave_whenDuplicateExpenseIsFound_thenItWhouldReturn400() throws Exception {
        URI uri = new URI("/despesas");
        String json = """
                {
                  "descricao": "Premio Duplicado",
                  "valor": 10000,
                  "data": "2021-08-21",
                  "categoria": "Alimentação"
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
                                  "data": "2021-08-21",
                                  "categoria": "Alimentação"
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
                             "error": "Despesa duplicada no mesmo mês"
                          }
                        ]"""));
    }

    @Test
    void givenSave_whenExpenseHasNoCategory_thenItWhouldAssignADefaultOfOutrasAndReturn201() throws Exception {
        URI uri = new URI("/despesas");
        String json = """
                {
                  "descricao": "Premio Sem Categoria",
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
                          "descricao": "Premio Sem Categoria",
                          "valor": 10000,
                          "data": "2021-08-21",
                          "categoria": "Outras"
                        }"""));
    }

    @Test
    void findByDescriptionOrAll() {
    }

    @Test
    void getDetail() {
    }

    @Test
    void findByYearAndMonth() {
    }

    @Test
    void update() {
    }

    @Test
    void delete() {
    }
}