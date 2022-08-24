package br.com.alexmdo.controleorcamentofamiliar.controller.form;

import br.com.alexmdo.controleorcamentofamiliar.exception.IncomeDuplicateException;
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

    public Despesa converter() {
        return new Despesa(null, getDescricao(), getValor(), getData());
    }

    public Despesa atualizar(long id, DespesaRepository despesaRepository) {
        Optional<Despesa> despesaOptional = despesaRepository.findById(id);
        if (despesaOptional.isPresent()) {
            Despesa despesa = despesaOptional.get();

            List<Despesa> despesas = despesaRepository.findByDescricao(getDescricao());
            List<Despesa> despesasFilteredByCurrentMonth = despesas
                    .stream()
                    .filter(obj -> obj.getData().getMonth() == LocalDate.now().getMonth())
                    .toList();
            if (!despesasFilteredByCurrentMonth.isEmpty()) {
                throw new IncomeDuplicateException("Despesa duplicada no mesmo mês");
            }

            despesa.setValor(getValor());
            despesa.setDescricao(getDescricao());
            despesa.setData(getData());

            return despesa;
        }

        return null;
    }
}
