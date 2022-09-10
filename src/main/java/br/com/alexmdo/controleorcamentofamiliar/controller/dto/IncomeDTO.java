package br.com.alexmdo.controleorcamentofamiliar.controller.dto;

import br.com.alexmdo.controleorcamentofamiliar.model.Income;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

public record IncomeDTO(Long id, String description, BigDecimal amount, LocalDate date) {

    public static List<IncomeDTO> converter(List<Income> incomes) {
        return incomes
                .stream()
                .map((income) -> new IncomeDTO(income.getId(), income.getDescription(), income.getAmount(), income.getDate()))
                .toList();
    }

}
