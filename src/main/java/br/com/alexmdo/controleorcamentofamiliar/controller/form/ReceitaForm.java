package br.com.alexmdo.controleorcamentofamiliar.controller.form;

import br.com.alexmdo.controleorcamentofamiliar.model.Receita;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.time.LocalDate;

@Data
public class ReceitaForm {

    @NotNull @NotEmpty @Length(min = 5)
    private String descricao;
    @NotNull
    private BigDecimal valor;
    @NotNull
    private LocalDate data;

    public Receita converter() {
        return new Receita(null, getDescricao(), getValor(), getData());
    }

}
