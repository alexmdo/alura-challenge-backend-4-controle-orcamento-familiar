package br.com.alexmdo.controleorcamentofamiliar.controller;

import br.com.alexmdo.controleorcamentofamiliar.model.Categoria;
import br.com.alexmdo.controleorcamentofamiliar.model.CategoriaId;
import br.com.alexmdo.controleorcamentofamiliar.model.CategoriaType;
import br.com.alexmdo.controleorcamentofamiliar.model.Despesa;
import br.com.alexmdo.controleorcamentofamiliar.repository.DespesaRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
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
class DespesaControllerTest {

    @SuppressWarnings("SpringJavaInjectionPointsAutowiringInspection")
    @Autowired
    MockMvc mockMvc;

    @Autowired
    DespesaRepository despesaRepository;

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
    void givenFindByDescriptionOrAll_whenExpenseIsFound_thenItShouldReturnOkAndAValidArrayResponse() throws Exception {
        URI uri = new URI("/despesas?descricao=ALU");

        mockMvc.perform(MockMvcRequestBuilders
                        .get(uri)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().json("""
                        [
                          	{
                          		"id": 1,
                          		"descricao": "ALUGUEL COTIA",
                          		"valor": 2000.00,
                          		"data": "2021-02-15",
                          		"categoria": "Moradia"
                          	},
                          	{
                          		"id": 3,
                          		"descricao": "ALURA",
                          		"valor": 1400.00,
                          		"data": "2021-02-19",
                          		"categoria": "Educação"
                          	}
                          ]"""));
    }

    @Test
    void givenFindByDescriptionOrAll_whenExpenseIsNotFound_thenItShouldReturnOkAndEmptyArrayResponse() throws Exception {
        URI uri = new URI("/despesas?descricao=XPTO");

        mockMvc.perform(MockMvcRequestBuilders
                        .get(uri)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().json("[]"));
    }

    @Test
    void givenFindByDescriptionOrAll_whenDescricaoRequestParamIsBlank_thenItShouldReturnOkAndAValidArrayResponse() throws Exception {
        URI uri = new URI("/despesas?descricao=");

        mockMvc.perform(MockMvcRequestBuilders
                        .get(uri)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().json("""
                        [
                           	{
                           		"id": 1,
                           		"descricao": "ALUGUEL COTIA",
                           		"valor": 2000.00,
                           		"data": "2021-02-15",
                           		"categoria": "Moradia"
                           	},
                           	{
                           		"id": 2,
                           		"descricao": "POSTO COMBUSTIVEL",
                           		"valor": 180.00,
                           		"data": "2021-02-03",
                           		"categoria": "Transporte"
                           	},
                           	{
                           		"id": 3,
                           		"descricao": "ALURA",
                           		"valor": 1400.00,
                           		"data": "2021-02-19",
                           		"categoria": "Educação"
                           	},
                           	{
                           		"id": 4,
                           		"descricao": "HAPPY HOUR",
                           		"valor": 120.00,
                           		"data": "2021-02-19",
                           		"categoria": "Lazer"
                           	}
                           ]"""));
    }

    @Test
    void givenGetDetail_whenExpenseIsFound_thenItShouldReturnOkAndValidResponse() throws Exception {
        URI uri = new URI("/despesas/1");

        mockMvc.perform(MockMvcRequestBuilders
                        .get(uri)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().json("""
                        {
                         	"id": 1,
                         	"descricao": "ALUGUEL COTIA",
                         	"valor": 2000.00,
                         	"data": "2021-02-15",
                         	"categoria": "Moradia"
                         }"""));
    }

    @Test
    void givenGetDetail_whenExpenseIsNotFound_thenItShouldReturnNotFoundAndNoResponse() throws Exception {
        URI uri = new URI("/despesas/99");

        mockMvc.perform(MockMvcRequestBuilders
                        .get(uri)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(content().string(""));
    }

    @Test
    void givenFindByYearAndMonth_whenExpenseIsFound_thenItShouldReturnOkAndAValidArrayResponse() throws Exception {
        URI uri = new URI("/despesas/2021/2");

        mockMvc.perform(MockMvcRequestBuilders
                        .get(uri)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().json("""
                        [
                           	{
                           		"id": 1,
                           		"descricao": "ALUGUEL COTIA",
                           		"valor": 2000.00,
                           		"data": "2021-02-15",
                           		"categoria": "Moradia"
                           	},
                           	{
                           		"id": 2,
                           		"descricao": "POSTO COMBUSTIVEL",
                           		"valor": 180.00,
                           		"data": "2021-02-03",
                           		"categoria": "Transporte"
                           	},
                           	{
                           		"id": 3,
                           		"descricao": "ALURA",
                           		"valor": 1400.00,
                           		"data": "2021-02-19",
                           		"categoria": "Educação"
                           	},
                           	{
                           		"id": 4,
                           		"descricao": "HAPPY HOUR",
                           		"valor": 120.00,
                           		"data": "2021-02-19",
                           		"categoria": "Lazer"
                           	}
                           ]"""));
    }

    @Test
    void givenFindByYearAndMonth_whenExpenseIsNotFound_thenItShouldReturnOkAndNoResponse() throws Exception {
        URI uri = new URI("/despesas/2021/12");

        mockMvc.perform(MockMvcRequestBuilders
                        .get(uri)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().json("[]"));
    }

    @Test
    void givenUpdate_whenExpenseIsFound_thenItShouldReturnOkAndAValidResponse() throws Exception {
        CategoriaId food = new CategoriaId("Alimentação", CategoriaType.DESPESA);
        Categoria categoria = new Categoria(food);
        Despesa despesaToDelete = new Despesa(null, "TO DELETE", BigDecimal.TEN, LocalDate.now(), categoria);
        despesaToDelete = despesaRepository.save(despesaToDelete);

        URI uri = new URI("/despesas/" + despesaToDelete.getId());

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
                            "data": "2021-08-21",
                         	"categoria": "Alimentação"
                         }"""));

        despesaRepository.deleteById(despesaToDelete.getId());
    }

    @Test
    void givenUpdate_whenExpenseIsFoundAndDuplicate_thenItShouldReturnBadRequestAndAValidErrorResponse() throws Exception {
        CategoriaId food = new CategoriaId("Alimentação", CategoriaType.DESPESA);
        Categoria categoria = new Categoria(food);
        Despesa despesaToDelete = new Despesa(null, "TO DELETE", BigDecimal.TEN, LocalDate.of(2021, 8, 21), categoria);
        despesaToDelete = despesaRepository.save(despesaToDelete);

        URI uri = new URI("/despesas/" + despesaToDelete.getId());

        mockMvc.perform(MockMvcRequestBuilders
                        .put(uri)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                            {
                              "descricao": "TO DELETE",
                              "valor": 10000,
                              "data": "2021-08-21"
                            }
                        """))
                .andExpect(status().isBadRequest())
                .andExpect(content().json("""
                        [
                            {
                                "field": "descricao",
                                "error": "Despesa duplicada no mesmo mês"
                            }
                        ]"""));

        despesaRepository.deleteById(despesaToDelete.getId());
    }

    @Test
    void givenUpdate_whenExpenseIsNotFoundAndDuplicate_thenItShouldReturnNotFoundAndAEmptyResponse() throws Exception {
        URI uri = new URI("/despesas/-1");

        mockMvc.perform(MockMvcRequestBuilders
                        .put(uri)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                            {
                              "descricao": "TO DELETE",
                              "valor": 10000,
                              "data": "2021-08-21"
                            }
                        """))
                .andExpect(status().isNotFound())
                .andExpect(content().string(""));
    }

    @Test
    void givenDelete_whenExpenseIsFound_thenItShouldReturnOkAndEmptyResponse() throws Exception {
        CategoriaId food = new CategoriaId("Alimentação", CategoriaType.DESPESA);
        Categoria categoria = new Categoria(food);
        Despesa despesaToDelete = new Despesa(null, "TO DELETE", BigDecimal.TEN, LocalDate.now(), categoria);
        despesaToDelete = despesaRepository.save(despesaToDelete);

        URI uri = new URI("/despesas/" + despesaToDelete.getId());

        mockMvc.perform(MockMvcRequestBuilders
                        .delete(uri)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().string(""));

        Optional<Despesa> optionalDespesa = despesaRepository.findById(despesaToDelete.getId());
        assertFalse(optionalDespesa.isPresent());
    }

    @Test
    void givenDelete_whenExpenseIsNotFound_thenItShouldReturnNotFoundAndEmptyResponse() throws Exception {
        URI uri = new URI("/despesas/-1");

        mockMvc.perform(MockMvcRequestBuilders
                        .delete(uri)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(content().string(""));
    }
}