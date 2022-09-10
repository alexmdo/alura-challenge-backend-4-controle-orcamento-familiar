package br.com.alexmdo.controleorcamentofamiliar.service;

import br.com.alexmdo.controleorcamentofamiliar.exception.IncomeDuplicateException;
import br.com.alexmdo.controleorcamentofamiliar.model.Category;
import br.com.alexmdo.controleorcamentofamiliar.model.CategoryId;
import br.com.alexmdo.controleorcamentofamiliar.model.CategoryType;
import br.com.alexmdo.controleorcamentofamiliar.model.Expense;
import br.com.alexmdo.controleorcamentofamiliar.model.vo.ExpenseByCategoryVO;
import br.com.alexmdo.controleorcamentofamiliar.repository.ExpenseRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;
import javax.transaction.Transactional;
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

    public List<ExpenseByCategoryVO> getTotalExpenseByCategory(Integer year, Integer month) {
        return expenseRepository.getTotalExpenseByCategory(year, month);
    }

    public List<Expense> findAll() {
        return expenseRepository.findAll();
    }

    public Optional<Expense> findById(Long id) {
        return expenseRepository.findById(id);
    }

    @Transactional
    public void deleteById(Long id) {
        expenseRepository.deleteById(id);
    }

    @Transactional
    public Expense save(Expense expense) {
        assignDefaultCategoryIfNoneProvided(expense);
        validateIfThereIsAnyDuplicate(expense);
        return expenseRepository.save(expense);
    }

    @Transactional
    public Expense update(Long id, Expense expenseToUpdate) {
        Optional<Expense> expenseOpt = this.findById(id);
        Expense expense = expenseOpt.orElseThrow(() -> new EntityNotFoundException("No expense found to update"));

        validateIfThereIsAnyDuplicate(expenseToUpdate);

        expense.setAmount(expenseToUpdate.getAmount());
        expense.setDescription(expenseToUpdate.getDescription());
        expense.setDate(expenseToUpdate.getDate());

        return expense;
    }

    private void assignDefaultCategoryIfNoneProvided(Expense expense) {
        if (expense.getCategory() == null || expense.getCategory().getId() == null || expense.getCategory().getId().getDescription() == null || expense.getCategory().getId().getDescription().isBlank()) {
            expense.setCategory(new Category(new CategoryId("Outras", CategoryType.EXPENSE)));
        }
    }

    private void validateIfThereIsAnyDuplicate(Expense expense) {
        List<Expense> expenses = this.findByYearMonthAndDescription(expense.getDate().getYear(), expense.getDate().getMonthValue(), expense.getDescription());
        if (!expenses.isEmpty()) {
            throw new IncomeDuplicateException("Duplicated expense in the same month");
        }
    }
}
