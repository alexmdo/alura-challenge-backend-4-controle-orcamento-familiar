package br.com.alexmdo.controleorcamentofamiliar.service;

import br.com.alexmdo.controleorcamentofamiliar.exception.IncomeDuplicateException;
import br.com.alexmdo.controleorcamentofamiliar.model.Income;
import br.com.alexmdo.controleorcamentofamiliar.repository.IncomeRepository;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;
import javax.transaction.Transactional;
import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@Service
public class IncomeService {

    private final IncomeRepository incomeRepository;

    public IncomeService(IncomeRepository incomeRepository) {
        this.incomeRepository = incomeRepository;
    }

    public List<Income> findByYearMonthAndDescription(int year, int monthValue, String description) {
        return incomeRepository.findByYearMonthAndDescription(year, monthValue, description);
    }

   public List<Income> findAll() {
        return incomeRepository.findAll();
    }

    public List<Income> findByDescriptionContaining(String description) {
        return incomeRepository.findByDescriptionContaining(description);
    }

    public List<Income> findByYearAndMonth(Integer year, Integer month) {
        return incomeRepository.findByYearAndMonth(year, month);
    }

    public Optional<Income> findById(Long id) {
        return incomeRepository.findById(id);
    }

    public BigDecimal getSumIncomesByYearAndMonth(Integer year, Integer month) {
        return incomeRepository.getSumIncomesByYearAndMonth(year, month);
    }

    @Transactional
    public Income save(Income income) {
        validateIfThereIsAnyDuplicate(income);

        return incomeRepository.save(income);
    }

    @Transactional
    public void deleteById(Long id) {
        incomeRepository.deleteById(id);
    }

    @Transactional
    public Income update(Long id, Income incomeToUpdate) {
        Optional<Income> receitaOptional = this.findById(id);
        Income income = receitaOptional.orElseThrow(() -> new EntityNotFoundException("No income found to update"));

        validateIfThereIsAnyDuplicate(incomeToUpdate);

        income.setAmount(incomeToUpdate.getAmount());
        income.setDescription(incomeToUpdate.getDescription());
        income.setDate(incomeToUpdate.getDate());

        return income;
    }

    private void validateIfThereIsAnyDuplicate(Income income) {
        List<Income> incomes = this.findByYearMonthAndDescription(income.getDate().getYear(), income.getDate().getMonthValue(), income.getDescription());
        if (!incomes.isEmpty()) {
            throw new IncomeDuplicateException("Duplicated income in the same month");
        }
    }
}
