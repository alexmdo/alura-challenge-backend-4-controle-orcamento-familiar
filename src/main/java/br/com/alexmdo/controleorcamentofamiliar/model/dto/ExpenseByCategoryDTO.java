package br.com.alexmdo.controleorcamentofamiliar.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
public class ExpenseByCategoryDTO {

    private String description;
    private BigDecimal total;

}
