package br.com.alexmdo.controleorcamentofamiliar.controller;

import br.com.alexmdo.controleorcamentofamiliar.model.dto.ExpenseByCategoryDTO;
import br.com.alexmdo.controleorcamentofamiliar.model.dto.SummaryOfTheMonthDTO;
import br.com.alexmdo.controleorcamentofamiliar.repository.ExpenseRepository;
import br.com.alexmdo.controleorcamentofamiliar.repository.IncomeRepository;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;
import java.util.List;

@RestController
@RequestMapping("/summary")
public class ReportController {

    private final ExpenseRepository expenseRepository;
    private final IncomeRepository incomeRepository;

    public ReportController(ExpenseRepository expenseRepository, IncomeRepository incomeRepository) {
        this.expenseRepository = expenseRepository;
        this.incomeRepository = incomeRepository;
    }

    @GetMapping("/{year}/{month}")
    public SummaryOfTheMonthDTO generateSummaryOfTheMonth(@PathVariable Integer year, @PathVariable Integer month) {
        BigDecimal sumExpensesByYearAndMonth = expenseRepository.getSumExpensesByYearAndMonth(year, month);
        BigDecimal sumIncomesByYearAndMonth = incomeRepository.getSumIncomesByYearAndMonth(year, month);
        BigDecimal balance = sumIncomesByYearAndMonth.subtract(sumExpensesByYearAndMonth);
        List<ExpenseByCategoryDTO> totalExpenseByCategory = expenseRepository.getTotalExpenseByCategory(year, month);

        return new SummaryOfTheMonthDTO(sumIncomesByYearAndMonth, sumExpensesByYearAndMonth, balance, totalExpenseByCategory);
    }

}
