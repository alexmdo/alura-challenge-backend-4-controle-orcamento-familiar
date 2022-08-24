package br.com.alexmdo.controleorcamentofamiliar.controller.form;

import br.com.alexmdo.controleorcamentofamiliar.exception.IncomeDuplicateException;
import br.com.alexmdo.controleorcamentofamiliar.model.Receita;
import br.com.alexmdo.controleorcamentofamiliar.repository.ReceitaRepository;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

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

    public Receita atualizar(long id, ReceitaRepository receitaRepository) {
        Optional<Receita> receitaOptional = receitaRepository.findById(id);
        if (receitaOptional.isPresent()) {
            Receita receita = receitaOptional.get();

            List<Receita> receitas = receitaRepository.findByDescricao(getDescricao());
            List<Receita> receitasFilteredByCurrentMonth = receitas
                    .stream()
                    .filter(obj -> obj.getData().getMonth() == LocalDate.now().getMonth())
                    .toList();
            if (!receitasFilteredByCurrentMonth.isEmpty()) {
                throw new IncomeDuplicateException("Receita duplicada no mesmo mês");
            }

            receita.setValor(getValor());
            receita.setDescricao(getDescricao());
            receita.setData(getData());

            return receita;
        }

        return null;
    }
}
