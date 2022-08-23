package br.com.alexmdo.controleorcamentofamiliar.controller;

import br.com.alexmdo.controleorcamentofamiliar.controller.dto.ReceitaDTO;
import br.com.alexmdo.controleorcamentofamiliar.controller.form.ReceitaForm;
import br.com.alexmdo.controleorcamentofamiliar.exception.IncomeDuplicateException;
import br.com.alexmdo.controleorcamentofamiliar.model.Receita;
import br.com.alexmdo.controleorcamentofamiliar.repository.ReceitaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.UriComponentsBuilder;

import javax.validation.Valid;
import java.net.URI;
import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@RestController
public class ReceitaController {

    private final ReceitaRepository receitaRepository;

    @Autowired
    public ReceitaController(ReceitaRepository receitaRepository) {
        this.receitaRepository = receitaRepository;
    }

    @PostMapping("/receitas")
    public ResponseEntity<ReceitaDTO> cadastrarReceita(@RequestBody @Valid ReceitaForm form, UriComponentsBuilder uriBuilder) {
        Receita receita = form.converter();

        List<Receita> receitas = receitaRepository.findByDescricao(receita.getDescricao());
        List<Receita> receitasFilteredByCurrentMonth = receitas
                .stream()
                .filter(obj -> obj.getData().getMonth() == LocalDate.now().getMonth())
                .toList();
        if (!receitasFilteredByCurrentMonth.isEmpty()) {
            throw new IncomeDuplicateException("Receita duplicada no mesmo mês");
        }

        receita = receitaRepository.save(receita);

        URI uri = uriBuilder.path("/receitas/{id}").buildAndExpand(receita.getId()).toUri();
        return ResponseEntity.created(uri).body(new ReceitaDTO(receita));
    }

}
