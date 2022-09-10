package br.com.alexmdo.controleorcamentofamiliar.model.dto;

import lombok.Data;

import java.math.BigDecimal;

public record ExpenseByCategoryDTO(String description, BigDecimal total) {

}
