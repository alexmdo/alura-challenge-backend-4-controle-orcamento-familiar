package br.com.alexmdo.controleorcamentofamiliar.controller;

import br.com.alexmdo.controleorcamentofamiliar.controller.dto.IncomeDTO;
import br.com.alexmdo.controleorcamentofamiliar.controller.form.IncomeForm;
import br.com.alexmdo.controleorcamentofamiliar.model.Income;
import br.com.alexmdo.controleorcamentofamiliar.service.IncomeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import javax.persistence.EntityNotFoundException;
import javax.validation.Valid;
import java.net.URI;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/incomes")
public class IncomeController {

    private final IncomeService incomeService;

    @Autowired
    public IncomeController(IncomeService incomeService) {
        this.incomeService = incomeService;
    }

    @PostMapping()
    public ResponseEntity<IncomeDTO> save(@RequestBody @Valid final IncomeForm form, final UriComponentsBuilder uriBuilder) {
        Income income = incomeService.save(form.adapt());
        URI uri = uriBuilder.path("/incomes/{id}").buildAndExpand(income.getId()).toUri();
        return ResponseEntity.created(uri).body(new IncomeDTO(income.getId(), income.getDescription(), income.getAmount(), income.getDate()));
    }

    @GetMapping()
    public List<IncomeDTO> findByDescriptionOrAll(@RequestParam(required = false) String description) {
        if (description == null || description.isBlank()) {
            return IncomeDTO.converter(incomeService.findAll());
        } else {
            return IncomeDTO.converter(incomeService.findByDescriptionContaining(description));
        }
    }

    @GetMapping("/{year}/{month}")
    public List<IncomeDTO> findByYearAndMonth(@PathVariable final Integer year, @PathVariable final Integer month) {
        return IncomeDTO.converter(incomeService.findByYearAndMonth(year, month));
    }

    @GetMapping("/{id}")
    public ResponseEntity<IncomeDTO> getDetail(@PathVariable final Long id) {
        Optional<Income> receitaOptional = incomeService.findById(id);
        return receitaOptional.map(receita -> ResponseEntity.ok(new IncomeDTO(receita.getId(), receita.getDescription(), receita.getAmount(), receita.getDate()))).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PutMapping("/{id}")
    public ResponseEntity<IncomeDTO> update(@PathVariable final Long id, @RequestBody @Valid final IncomeForm form) {
        try {
            Income income = incomeService.update(id, form.adapt());
            return ResponseEntity.ok(new IncomeDTO(income.getId(), income.getDescription(), income.getAmount(), income.getDate()));
        } catch (EntityNotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<IncomeDTO> delete(@PathVariable final Long id) {
        try {
            incomeService.deleteById(id);
            return ResponseEntity.ok().build();
        } catch (EmptyResultDataAccessException e) {
            return ResponseEntity.notFound().build();
        }
    }

}
