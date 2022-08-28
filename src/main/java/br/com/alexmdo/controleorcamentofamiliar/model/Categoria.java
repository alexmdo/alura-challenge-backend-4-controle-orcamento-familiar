package br.com.alexmdo.controleorcamentofamiliar.model;

import lombok.*;
import org.hibernate.Hibernate;

import javax.persistence.*;
import java.util.Objects;

@Entity
@Table(name = "categorias")
@ToString
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Categoria {

    @EmbeddedId
    private CategoriaId id;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        Categoria categoria = (Categoria) o;
        return id != null && Objects.equals(id, categoria.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}