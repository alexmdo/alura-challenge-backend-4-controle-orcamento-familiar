package br.com.alexmdo.controleorcamentofamiliar.controller.form;

import br.com.alexmdo.controleorcamentofamiliar.model.Income;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.time.LocalDate;

@Data
public class IncomeForm {

    @NotNull @NotEmpty @Length(min = 5)
    private String description;
    @NotNull
    private BigDecimal amount;
    @NotNull
    private LocalDate date;

    public Income adapt() {
        return new Income(null, getDescription(), getAmount(), getDate());
    }

}
