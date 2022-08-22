package br.com.alexmdo.controleorcamentofamiliar.controller;

import br.com.alexmdo.controleorcamentofamiliar.controller.dto.ReceitaDTO;
import br.com.alexmdo.controleorcamentofamiliar.controller.dto.ReceitaForm;
import br.com.alexmdo.controleorcamentofamiliar.model.Receita;
import br.com.alexmdo.controleorcamentofamiliar.repository.ReceitaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;

@RestController
public class ReceitaController {

    private ReceitaRepository receitaRepository;

    @Autowired
    public ReceitaController(ReceitaRepository receitaRepository) {
        this.receitaRepository = receitaRepository;
    }

    @PostMapping("/receitas")
    public ResponseEntity<ReceitaDTO> cadastrarReceita(@RequestBody ReceitaForm form, UriComponentsBuilder uriBuilder) {
        Receita receita = form.converter();
        receita = receitaRepository.save(receita);

        URI uri = uriBuilder.path("/receitas/{id}").buildAndExpand(receita.getId()).toUri();
        return ResponseEntity.created(uri).body(new ReceitaDTO(receita));
    }

}
