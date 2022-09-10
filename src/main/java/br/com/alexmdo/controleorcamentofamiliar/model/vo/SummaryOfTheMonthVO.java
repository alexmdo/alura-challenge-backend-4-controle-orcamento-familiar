package br.com.alexmdo.controleorcamentofamiliar.model.vo;

import java.math.BigDecimal;
import java.util.List;

public record SummaryOfTheMonthVO(BigDecimal totalIncome, BigDecimal totalExpense, BigDecimal balance,
                                  List<ExpenseByCategoryVO> expenseByCategory) {

}
