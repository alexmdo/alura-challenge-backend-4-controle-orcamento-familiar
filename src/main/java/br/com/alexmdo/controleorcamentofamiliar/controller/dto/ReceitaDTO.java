package br.com.alexmdo.controleorcamentofamiliar.controller.dto;

import br.com.alexmdo.controleorcamentofamiliar.model.Receita;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Data
public class ReceitaDTO {

    private Long id;
    private String descricao;
    private BigDecimal valor;
    private LocalDate data;

    public ReceitaDTO(Receita receita) {
        this.id = receita.getId();
        this.descricao = receita.getDescricao();
        this.valor = receita.getValor();
        this.data = receita.getData();
    }

    public static List<ReceitaDTO> converter(List<Receita> receitas) {
        return receitas.stream().map(ReceitaDTO::new).toList();
    }

    public Receita converter() {
        return new Receita(getId(), getDescricao(), getValor(), getData());
    }

}
