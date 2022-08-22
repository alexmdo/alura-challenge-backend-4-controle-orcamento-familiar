package br.com.alexmdo.controleorcamentofamiliar.controller.dto;

import br.com.alexmdo.controleorcamentofamiliar.model.Receita;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
public class ReceitaForm {

    private String descricao;
    private BigDecimal valor;
    private LocalDate data;

    public Receita converter() {
        return new Receita(null, getDescricao(), getValor(), getData());
    }

}
