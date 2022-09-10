package br.com.alexmdo.controleorcamentofamiliar.service;

import br.com.alexmdo.controleorcamentofamiliar.model.Income;
import br.com.alexmdo.controleorcamentofamiliar.repository.IncomeRepository;
import org.springframework.stereotype.Service;

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

    public Income save(Income income) {
        return incomeRepository.save(income);
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

    public void deleteById(Long id) {
        incomeRepository.deleteById(id);
    }

    public BigDecimal getSumIncomesByYearAndMonth(Integer year, Integer month) {
        return incomeRepository.getSumIncomesByYearAndMonth(year, month);
    }

}
