package br.com.alexmdo.controleorcamentofamiliar.controller;

import br.com.alexmdo.controleorcamentofamiliar.controller.dto.DespesaDTO;
import br.com.alexmdo.controleorcamentofamiliar.controller.form.DespesaForm;
import br.com.alexmdo.controleorcamentofamiliar.exception.IncomeDuplicateException;
import br.com.alexmdo.controleorcamentofamiliar.model.Despesa;
import br.com.alexmdo.controleorcamentofamiliar.repository.DespesaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import javax.transaction.Transactional;
import javax.validation.Valid;
import java.net.URI;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/despesas")
public class DespesaController {

    private final DespesaRepository despesaRepository;

    @Autowired
    public DespesaController(DespesaRepository despesaRepository) {
        this.despesaRepository = despesaRepository;
    }

    @PostMapping()
    @Transactional
    public ResponseEntity<DespesaDTO> save(@RequestBody @Valid final DespesaForm form, final UriComponentsBuilder uriBuilder) {
        Despesa despesa = form.salvar(despesaRepository);
        URI uri = uriBuilder.path("/despesas/{id}").buildAndExpand(despesa.getId()).toUri();
        return ResponseEntity.created(uri).body(new DespesaDTO(despesa));
    }

    @GetMapping()
    public List<DespesaDTO> findAll() {
        return DespesaDTO.converter(despesaRepository.findAll());
    }
    @GetMapping("/{id}")
    public ResponseEntity<DespesaDTO> getDetail(@PathVariable final Long id) {
        Optional<Despesa> despesaOptional = despesaRepository.findById(id);
        return despesaOptional.map(despesa -> ResponseEntity.ok(new DespesaDTO(despesa))).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PutMapping("/{id}")
    @Transactional
    public ResponseEntity<DespesaDTO> update(@PathVariable final Long id, @RequestBody @Valid final DespesaForm form) {
        Optional<Despesa> despesaOptional = despesaRepository.findById(id);
        if (despesaOptional.isPresent()) {
            Despesa despesa = form.atualizar(id, despesaRepository);
            return ResponseEntity.ok(new DespesaDTO(despesa));
        }

        return ResponseEntity.notFound().build();
    }

    @DeleteMapping("/{id}")
    @Transactional
    public ResponseEntity<DespesaDTO> delete(@PathVariable final Long id) {
        Optional<Despesa> despesaOptional = despesaRepository.findById(id);
        if (despesaOptional.isPresent()) {
            despesaRepository.deleteById(id);
            return ResponseEntity.ok().build();
        }

        return ResponseEntity.notFound().build();
    }

}
