package br.com.alexmdo.controleorcamentofamiliar.controller.form;

import br.com.alexmdo.controleorcamentofamiliar.model.Category;
import br.com.alexmdo.controleorcamentofamiliar.model.CategoryId;
import br.com.alexmdo.controleorcamentofamiliar.model.CategoryType;
import br.com.alexmdo.controleorcamentofamiliar.model.Expense;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.time.LocalDate;

@Data
public class ExpenseForm {

    @NotNull @NotEmpty @Length(min = 5)
    private String description;
    @NotNull
    private BigDecimal amount;
    @NotNull
    private LocalDate date;
    private String category;

    public Expense adapt() {
        return new Expense(null, getDescription(), getAmount(), getDate(), new Category(new CategoryId(category, CategoryType.EXPENSE)));
    }

}
