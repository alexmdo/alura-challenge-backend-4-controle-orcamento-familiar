package br.com.alexmdo.controleorcamentofamiliar.controller.form;

import br.com.alexmdo.controleorcamentofamiliar.exception.IncomeDuplicateException;
import br.com.alexmdo.controleorcamentofamiliar.model.Categoria;
import br.com.alexmdo.controleorcamentofamiliar.model.CategoriaId;
import br.com.alexmdo.controleorcamentofamiliar.model.CategoriaType;
import br.com.alexmdo.controleorcamentofamiliar.model.Despesa;
import br.com.alexmdo.controleorcamentofamiliar.repository.DespesaRepository;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Data
public class DespesaForm {

    @NotNull @NotEmpty @Length(min = 5)
    private String descricao;
    @NotNull
    private BigDecimal valor;
    @NotNull
    private LocalDate data;
    private String categoria;

    public Despesa converter() {
        if (categoria == null || categoria.isBlank()) {
            this.categoria = "Outras";
        }

        return new Despesa(null, getDescricao(), getValor(), getData(), new Categoria(new CategoriaId(categoria, CategoriaType.DESPESA)));
    }

    public Despesa atualizar(long id, DespesaForm form, DespesaRepository despesaRepository) {
        Optional<Despesa> despesaOptional = despesaRepository.findById(id);
        if (despesaOptional.isPresent()) {
            Despesa despesa = despesaOptional.get();

            List<Despesa> despesas = despesaRepository.findByYearMonthAndDescription(form.getData().getYear(), form.getData().getMonthValue(), form.getDescricao());
            if (!despesas.isEmpty()) {
                throw new IncomeDuplicateException("Despesa duplicada no mesmo mês");
            }

            despesa.setValor(getValor());
            despesa.setDescricao(getDescricao());
            despesa.setData(getData());

            return despesa;
        }

        return null;
    }

    public Despesa salvar(DespesaRepository despesaRepository) {
        Despesa despesa = this.converter();

        List<Despesa> despesas = despesaRepository.findByYearMonthAndDescription(despesa.getData().getYear(), despesa.getData().getMonthValue(), despesa.getDescricao());
        if (!despesas.isEmpty()) {
            throw new IncomeDuplicateException("Despesa duplicada no mesmo mês");
        }

        return despesaRepository.save(despesa);
    }
}
