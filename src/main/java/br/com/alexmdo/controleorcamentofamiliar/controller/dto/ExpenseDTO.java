package br.com.alexmdo.controleorcamentofamiliar.controller.dto;

import br.com.alexmdo.controleorcamentofamiliar.model.Expense;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Data
public class ExpenseDTO {

    private Long id;
    private String description;
    private BigDecimal amount;
    private LocalDate date;
    private String category;

    public ExpenseDTO(Expense expense) {
        this.id = expense.getId();
        this.description = expense.getDescription();
        this.amount = expense.getAmount();
        this.date = expense.getDate();
        this.category = expense.getCategory() != null && expense.getCategory().getId() != null ? expense.getCategory().getId().getDescription() : null;
    }

    public static List<ExpenseDTO> converter(List<Expense> receitas) {
        return receitas.stream().map(ExpenseDTO::new).toList();
    }

}
