package br.com.alexmdo.controleorcamentofamiliar.model;

import lombok.*;
import org.hibernate.Hibernate;

import javax.persistence.Embeddable;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import java.io.Serial;
import java.io.Serializable;
import java.util.Objects;

@Embeddable
@ToString
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CategoryId implements Serializable {

    @Serial
    private static final long serialVersionUID = 3140930925599382735L;

    private String description;
    @Enumerated(EnumType.STRING)
    private CategoryType categoryType;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        CategoryId that = (CategoryId) o;
        return description != null && Objects.equals(description, that.description)
                && categoryType != null && Objects.equals(categoryType, that.categoryType);
    }

    @Override
    public int hashCode() {
        return Objects.hash(description, categoryType);
    }
}