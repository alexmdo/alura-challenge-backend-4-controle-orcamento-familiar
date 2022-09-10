package br.com.alexmdo.controleorcamentofamiliar.controller;

import br.com.alexmdo.controleorcamentofamiliar.controller.dto.ExpenseDTO;
import br.com.alexmdo.controleorcamentofamiliar.controller.form.ExpenseForm;
import br.com.alexmdo.controleorcamentofamiliar.model.Expense;
import br.com.alexmdo.controleorcamentofamiliar.service.ExpenseService;
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
@RequestMapping("/expenses")
public class ExpenseController {

    private final ExpenseService expenseService;

    @Autowired
    public ExpenseController(ExpenseService expenseService) {
        this.expenseService = expenseService;
    }

    @PostMapping()
    public ResponseEntity<ExpenseDTO> save(@RequestBody @Valid final ExpenseForm form, final UriComponentsBuilder uriBuilder) {
        Expense expense = expenseService.save(form.adapt());
        URI uri = uriBuilder.path("/expenses/{id}").buildAndExpand(expense.getId()).toUri();
        return ResponseEntity.created(uri).body(new ExpenseDTO(expense.getId(), expense.getDescription(), expense.getAmount(), expense.getDate(), expense.getCategory().getId().getDescription()));
    }

    @GetMapping()
    public List<ExpenseDTO> findByDescriptionOrAll(@RequestParam(required = false) String descricao) {
        if (descricao == null || descricao.isBlank()) {
            return ExpenseDTO.adapt(expenseService.findAll());
        } else {
            return ExpenseDTO.adapt(expenseService.findByDescriptionContaining(descricao));
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<ExpenseDTO> getDetail(@PathVariable final Long id) {
        Optional<Expense> despesaOptional = expenseService.findById(id);
        return despesaOptional.map(despesa -> ResponseEntity.ok(new ExpenseDTO(despesa.getId(), despesa.getDescription(), despesa.getAmount(), despesa.getDate(), despesa.getCategory().getId().getDescription()))).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @GetMapping("/{year}/{month}")
    public List<ExpenseDTO> findByYearAndMonth(@PathVariable final Integer year, @PathVariable final Integer month) {
        return ExpenseDTO.adapt(expenseService.findByYearAndMonth(year, month));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ExpenseDTO> update(@PathVariable final Long id, @RequestBody @Valid final ExpenseForm form) {
        try {
            Expense expense = expenseService.update(id, form.adapt());
            return ResponseEntity.ok(new ExpenseDTO(expense.getId(), expense.getDescription(), expense.getAmount(), expense.getDate(), expense.getCategory().getId().getDescription()));
        } catch (EntityNotFoundException ex) {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ExpenseDTO> delete(@PathVariable final Long id) {
        try {
            expenseService.deleteById(id);
            return ResponseEntity.ok().build();
        } catch (EmptyResultDataAccessException e) {
            return ResponseEntity.notFound().build();
        }
    }

}
