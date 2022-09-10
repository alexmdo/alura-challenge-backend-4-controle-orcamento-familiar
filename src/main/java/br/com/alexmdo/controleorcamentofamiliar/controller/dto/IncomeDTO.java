package br.com.alexmdo.controleorcamentofamiliar.controller.dto;

import br.com.alexmdo.controleorcamentofamiliar.model.Income;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Data
public class IncomeDTO {

    private Long id;
    private String description;
    private BigDecimal amount;
    private LocalDate date;

    public IncomeDTO(Income income) {
        this.id = income.getId();
        this.description = income.getDescription();
        this.amount = income.getAmount();
        this.date = income.getDate();
    }

    public static List<IncomeDTO> converter(List<Income> incomes) {
        return incomes.stream().map(IncomeDTO::new).toList();
    }

    public Income converter() {
        return new Income(getId(), getDescription(), getAmount(), getDate());
    }

}
