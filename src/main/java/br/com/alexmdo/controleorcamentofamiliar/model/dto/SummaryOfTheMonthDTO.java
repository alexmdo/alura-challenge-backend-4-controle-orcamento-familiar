package br.com.alexmdo.controleorcamentofamiliar.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

@Data
@AllArgsConstructor
public class SummaryOfTheMonthDTO {

    private BigDecimal totalIncome;
    private BigDecimal totalExpense;
    private BigDecimal balance;
    private List<ExpenseByCategoryDTO> expenseByCategory;

}
