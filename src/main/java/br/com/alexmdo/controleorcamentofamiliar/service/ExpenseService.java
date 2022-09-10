package br.com.alexmdo.controleorcamentofamiliar.service;

import br.com.alexmdo.controleorcamentofamiliar.model.Expense;
import br.com.alexmdo.controleorcamentofamiliar.model.dto.ExpenseByCategoryDTO;
import br.com.alexmdo.controleorcamentofamiliar.repository.ExpenseRepository;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@Service
public class ExpenseService {

    private final ExpenseRepository expenseRepository;

    public ExpenseService(ExpenseRepository expenseRepository) {
        this.expenseRepository = expenseRepository;
    }

    public List<Expense> findByYearMonthAndDescription(Integer year, Integer month, String description) {
        return expenseRepository.findByYearMonthAndDescription(year, month, description);
    }

    public List<Expense> findByDescriptionContaining(String descricao) {
        return expenseRepository.findByDescriptionContaining(descricao);
    }

    public List<Expense> findByYearAndMonth(Integer year, Integer month) {
        return expenseRepository.findByYearAndMonth(year, month);
    }

    public BigDecimal getSumExpensesByYearAndMonth(Integer year, Integer month) {
        return expenseRepository.getSumExpensesByYearAndMonth(year, month);
    }

    public List<ExpenseByCategoryDTO> getTotalExpenseByCategory(Integer year, Integer month) {
        return expenseRepository.getTotalExpenseByCategory(year, month);
    }

    public List<Expense> findAll() {
        return expenseRepository.findAll();
    }

    public Optional<Expense> findById(Long id) {
        return expenseRepository.findById(id);
    }

    public void deleteById(Long id) {
        expenseRepository.deleteById(id);
    }

    public Expense save(Expense expense) {
        return expenseRepository.save(expense);
    }

}
