package br.com.alexmdo.controleorcamentofamiliar.controller.dto;

import br.com.alexmdo.controleorcamentofamiliar.model.Expense;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

public record ExpenseDTO(Long id, String description, BigDecimal amount, LocalDate date, String category) {

    public static List<ExpenseDTO> converter(List<Expense> receitas) {
        return receitas
                .stream()
                .map((expense) -> new ExpenseDTO(expense.getId(), expense.getDescription(), expense.getAmount(), expense.getDate(), expense.getCategory().getId().getDescription()))
                .toList();
    }

}
