package br.com.alexmdo.controleorcamentofamiliar.controller;

import br.com.alexmdo.controleorcamentofamiliar.controller.dto.ReceitaDTO;
import br.com.alexmdo.controleorcamentofamiliar.controller.form.ReceitaForm;
import br.com.alexmdo.controleorcamentofamiliar.exception.IncomeDuplicateException;
import br.com.alexmdo.controleorcamentofamiliar.model.Receita;
import br.com.alexmdo.controleorcamentofamiliar.repository.ReceitaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import javax.transaction.Transactional;
import javax.validation.Valid;
import java.net.URI;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/receitas")
public class ReceitaController {

    private final ReceitaRepository receitaRepository;

    @Autowired
    public ReceitaController(ReceitaRepository receitaRepository) {
        this.receitaRepository = receitaRepository;
    }

    @PostMapping()
    @Transactional
    public ResponseEntity<ReceitaDTO> save(@RequestBody @Valid final ReceitaForm form, final UriComponentsBuilder uriBuilder) {
        Receita receita = form.converter();

        List<Receita> receitas = receitaRepository.findByYearMonthAndDescription(receita.getData().getYear(), receita.getData().getMonthValue(), receita.getDescricao());
        if (!receitas.isEmpty()) {
            throw new IncomeDuplicateException("Receita duplicada no mesmo mês");
        }

        receita = receitaRepository.save(receita);

        URI uri = uriBuilder.path("/receitas/{id}").buildAndExpand(receita.getId()).toUri();
        return ResponseEntity.created(uri).body(new ReceitaDTO(receita));
    }

    @GetMapping()
    public List<ReceitaDTO> findByDescriptionOrAll(@RequestParam(required = false) String descricao) {
        if (descricao == null || descricao.isBlank()) {
            return ReceitaDTO.converter(receitaRepository.findAll());
        } else {
            return ReceitaDTO.converter(receitaRepository.findByDescricaoContaining(descricao));
        }
    }

    @GetMapping("/{ano}/{mes}")
    public List<ReceitaDTO> findByYearAndMonth(@PathVariable final Integer ano, @PathVariable final Integer mes) {
        return ReceitaDTO.converter(receitaRepository.findByYearAndMonth(ano, mes));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ReceitaDTO> getDetail(@PathVariable final Long id) {
        Optional<Receita> receitaOptional = receitaRepository.findById(id);
        return receitaOptional.map(receita -> ResponseEntity.ok(new ReceitaDTO(receita))).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PutMapping("/{id}")
    @Transactional
    public ResponseEntity<ReceitaDTO> update(@PathVariable final Long id, @RequestBody @Valid final ReceitaForm form) {
        Optional<Receita> receitaOptional = receitaRepository.findById(id);
        if (receitaOptional.isPresent()) {
            Receita receita = form.atualizar(id, receitaRepository);
            return ResponseEntity.ok(new ReceitaDTO(receita));
        }

        return ResponseEntity.notFound().build();
    }

    @DeleteMapping("/{id}")
    @Transactional
    public ResponseEntity<ReceitaDTO> delete(@PathVariable final Long id) {
        Optional<Receita> receitaOptional = receitaRepository.findById(id);
        if (receitaOptional.isPresent()) {
            receitaRepository.deleteById(id);
            return ResponseEntity.ok().build();
        }

        return ResponseEntity.notFound().build();
    }

}
