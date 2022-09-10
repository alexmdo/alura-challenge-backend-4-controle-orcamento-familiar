package br.com.alexmdo.controleorcamentofamiliar.controller;

import br.com.alexmdo.controleorcamentofamiliar.model.dto.ExpenseByCategoryDTO;
import br.com.alexmdo.controleorcamentofamiliar.model.dto.SummaryOfTheMonthDTO;
import br.com.alexmdo.controleorcamentofamiliar.repository.IncomeRepository;
import br.com.alexmdo.controleorcamentofamiliar.service.ExpenseService;
import br.com.alexmdo.controleorcamentofamiliar.service.IncomeService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;
import java.util.List;

@RestController
@RequestMapping("/summary")
public class ReportController {

    private final ExpenseService expenseService;
    private final IncomeService incomeService;

    public ReportController(ExpenseService expenseService, IncomeService incomeService) {
        this.expenseService = expenseService;
        this.incomeService = incomeService;
    }

    @GetMapping("/{year}/{month}")
    public SummaryOfTheMonthDTO generateSummaryOfTheMonth(@PathVariable Integer year, @PathVariable Integer month) {
        BigDecimal sumExpensesByYearAndMonth = expenseService.getSumExpensesByYearAndMonth(year, month);
        BigDecimal sumIncomesByYearAndMonth = incomeService.getSumIncomesByYearAndMonth(year, month);
        BigDecimal balance = sumIncomesByYearAndMonth.subtract(sumExpensesByYearAndMonth);
        List<ExpenseByCategoryDTO> totalExpenseByCategory = expenseService.getTotalExpenseByCategory(year, month);

        return new SummaryOfTheMonthDTO(sumIncomesByYearAndMonth, sumExpensesByYearAndMonth, balance, totalExpenseByCategory);
    }

}
