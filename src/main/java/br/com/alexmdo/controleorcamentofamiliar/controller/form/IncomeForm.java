package br.com.alexmdo.controleorcamentofamiliar.controller.form;

import br.com.alexmdo.controleorcamentofamiliar.exception.IncomeDuplicateException;
import br.com.alexmdo.controleorcamentofamiliar.model.Income;
import br.com.alexmdo.controleorcamentofamiliar.repository.IncomeRepository;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Data
public class IncomeForm {

    @NotNull @NotEmpty @Length(min = 5)
    private String description;
    @NotNull
    private BigDecimal amount;
    @NotNull
    private LocalDate date;

    public Income converter() {
        return new Income(null, getDescription(), getAmount(), getDate());
    }

    public Income atualizar(long id, IncomeForm form, IncomeRepository incomeRepository) {
        Optional<Income> receitaOptional = incomeRepository.findById(id);
        if (receitaOptional.isPresent()) {
            Income income = receitaOptional.get();

            List<Income> incomes = incomeRepository.findByYearMonthAndDescription(form.getDate().getYear(), form.getDate().getMonthValue(), form.getDescription());
            if (!incomes.isEmpty()) {
                throw new IncomeDuplicateException("Duplicate income in the same month");
            }

            income.setAmount(getAmount());
            income.setDescription(getDescription());
            income.setDate(getDate());

            return income;
        }

        return null;
    }
}
