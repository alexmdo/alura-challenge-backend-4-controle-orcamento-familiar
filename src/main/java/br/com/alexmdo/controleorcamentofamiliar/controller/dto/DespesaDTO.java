package br.com.alexmdo.controleorcamentofamiliar.controller.dto;

import br.com.alexmdo.controleorcamentofamiliar.model.Despesa;
import br.com.alexmdo.controleorcamentofamiliar.model.Receita;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Data
public class DespesaDTO {

    private Long id;
    private String descricao;
    private BigDecimal valor;
    private LocalDate data;
    private String categoria;

    public DespesaDTO(Despesa despesa) {
        this.id = despesa.getId();
        this.descricao = despesa.getDescricao();
        this.valor = despesa.getValor();
        this.data = despesa.getData();
        this.categoria = despesa.getCategoria() != null && despesa.getCategoria().getId() != null ? despesa.getCategoria().getId().getDescricao() : null;
    }

    public static List<DespesaDTO> converter(List<Despesa> receitas) {
        return receitas.stream().map(DespesaDTO::new).toList();
    }

}
