package br.com.alexmdo.controleorcamentofamiliar.model;

import lombok.*;
import org.hibernate.Hibernate;

import javax.persistence.*;
import java.util.Objects;

@Entity
@Table(name = "categories")
@ToString
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Category {

    @EmbeddedId
    private CategoryId id;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        Category category = (Category) o;
        return id != null && Objects.equals(id, category.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}