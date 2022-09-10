package br.com.alexmdo.controleorcamentofamiliar.model.dto;

import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

public record SummaryOfTheMonthDTO(BigDecimal totalIncome, BigDecimal totalExpense, BigDecimal balance,
                                   List<ExpenseByCategoryDTO> expenseByCategory) {

}
