package br.com.alexmdo.controleorcamentofamiliar.controller.form;

import br.com.alexmdo.controleorcamentofamiliar.exception.IncomeDuplicateException;
import br.com.alexmdo.controleorcamentofamiliar.model.Category;
import br.com.alexmdo.controleorcamentofamiliar.model.CategoryId;
import br.com.alexmdo.controleorcamentofamiliar.model.CategoryType;
import br.com.alexmdo.controleorcamentofamiliar.model.Expense;
import br.com.alexmdo.controleorcamentofamiliar.repository.ExpenseRepository;
import br.com.alexmdo.controleorcamentofamiliar.service.ExpenseService;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Data
public class ExpenseForm {

    @NotNull @NotEmpty @Length(min = 5)
    private String description;
    @NotNull
    private BigDecimal amount;
    @NotNull
    private LocalDate date;
    private String category;

    public Expense converter() {
        if (category == null || category.isBlank()) {
            this.category = "Outras";
        }

        return new Expense(null, getDescription(), getAmount(), getDate(), new Category(new CategoryId(category, CategoryType.EXPENSE)));
    }

    public Expense update(long id, ExpenseForm form, ExpenseService expenseService) {
        Optional<Expense> despesaOptional = expenseService.findById(id);
        if (despesaOptional.isPresent()) {
            Expense expense = despesaOptional.get();

            List<Expense> expenses = expenseService.findByYearMonthAndDescription(form.getDate().getYear(), form.getDate().getMonthValue(), form.getDescription());
            if (!expenses.isEmpty()) {
                throw new IncomeDuplicateException("Duplicated expense in the same month");
            }

            expense.setAmount(getAmount());
            expense.setDescription(getDescription());
            expense.setDate(getDate());

            return expense;
        }

        return null;
    }

    public Expense save(ExpenseService expenseService) {
        Expense expense = this.converter();

        List<Expense> expenses = expenseService.findByYearMonthAndDescription(expense.getDate().getYear(), expense.getDate().getMonthValue(), expense.getDescription());
        if (!expenses.isEmpty()) {
            throw new IncomeDuplicateException("Duplicated expense in the same month");
        }

        return expenseService.save(expense);
    }
}
