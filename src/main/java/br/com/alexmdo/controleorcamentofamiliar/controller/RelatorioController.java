package br.com.alexmdo.controleorcamentofamiliar.controller;

import br.com.alexmdo.controleorcamentofamiliar.model.dto.ExpenseByCategoryDTO;
import br.com.alexmdo.controleorcamentofamiliar.model.dto.SummaryOfTheMonthDTO;
import br.com.alexmdo.controleorcamentofamiliar.repository.DespesaRepository;
import br.com.alexmdo.controleorcamentofamiliar.repository.ReceitaRepository;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;
import java.util.List;

@RestController
@RequestMapping("/resumo")
public class RelatorioController {

    private final DespesaRepository despesaRepository;
    private final ReceitaRepository receitaRepository;

    public RelatorioController(DespesaRepository despesaRepository, ReceitaRepository receitaRepository) {
        this.despesaRepository = despesaRepository;
        this.receitaRepository = receitaRepository;
    }

    @GetMapping("/{year}/{month}")
    public SummaryOfTheMonthDTO generateSummaryOfTheMonth(@PathVariable Integer year, @PathVariable Integer month) {
        BigDecimal sumExpensesByYearAndMonth = despesaRepository.getSumExpensesByYearAndMonth(year, month);
        BigDecimal sumIncomesByYearAndMonth = receitaRepository.getSumIncomesByYearAndMonth(year, month);
        BigDecimal balance = sumIncomesByYearAndMonth.subtract(sumExpensesByYearAndMonth);
        List<ExpenseByCategoryDTO> totalExpenseByCategory = despesaRepository.getTotalExpenseByCategory(year, month);

        return new SummaryOfTheMonthDTO(sumIncomesByYearAndMonth, sumExpensesByYearAndMonth, balance, totalExpenseByCategory);
    }

}
