package br.com.alexmdo.controleorcamentofamiliar.controller;

import br.com.alexmdo.controleorcamentofamiliar.controller.dto.ExpenseDTO;
import br.com.alexmdo.controleorcamentofamiliar.controller.form.ExpenseForm;
import br.com.alexmdo.controleorcamentofamiliar.model.Expense;
import br.com.alexmdo.controleorcamentofamiliar.repository.ExpenseRepository;
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
@RequestMapping("/expenses")
public class ExpenseController {

    private final ExpenseRepository expenseRepository;

    @Autowired
    public ExpenseController(ExpenseRepository expenseRepository) {
        this.expenseRepository = expenseRepository;
    }

    @PostMapping()
    @Transactional
    public ResponseEntity<ExpenseDTO> save(@RequestBody @Valid final ExpenseForm form, final UriComponentsBuilder uriBuilder) {
        Expense expense = form.save(expenseRepository);
        URI uri = uriBuilder.path("/expenses/{id}").buildAndExpand(expense.getId()).toUri();
        return ResponseEntity.created(uri).body(new ExpenseDTO(expense));
    }

    @GetMapping()
    public List<ExpenseDTO> findByDescriptionOrAll(@RequestParam(required = false) String descricao) {
        if (descricao == null || descricao.isBlank()) {
            return ExpenseDTO.converter(expenseRepository.findAll());
        } else {
            return ExpenseDTO.converter(expenseRepository.findByDescriptionContaining(descricao));
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<ExpenseDTO> getDetail(@PathVariable final Long id) {
        Optional<Expense> despesaOptional = expenseRepository.findById(id);
        return despesaOptional.map(despesa -> ResponseEntity.ok(new ExpenseDTO(despesa))).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @GetMapping("/{year}/{month}")
    public List<ExpenseDTO> findByYearAndMonth(@PathVariable final Integer year, @PathVariable final Integer month) {
        return ExpenseDTO.converter(expenseRepository.findByYearAndMonth(year, month));
    }

    @PutMapping("/{id}")
    @Transactional
    public ResponseEntity<ExpenseDTO> update(@PathVariable final Long id, @RequestBody @Valid final ExpenseForm form) {
        Optional<Expense> despesaOptional = expenseRepository.findById(id);
        if (despesaOptional.isPresent()) {
            Expense expense = form.update(id, form, expenseRepository);
            return ResponseEntity.ok(new ExpenseDTO(expense));
        }

        return ResponseEntity.notFound().build();
    }

    @DeleteMapping("/{id}")
    @Transactional
    public ResponseEntity<ExpenseDTO> delete(@PathVariable final Long id) {
        Optional<Expense> despesaOptional = expenseRepository.findById(id);
        if (despesaOptional.isPresent()) {
            expenseRepository.deleteById(id);
            return ResponseEntity.ok().build();
        }

        return ResponseEntity.notFound().build();
    }

}
