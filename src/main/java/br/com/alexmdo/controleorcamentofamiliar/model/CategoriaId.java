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
public class CategoriaId implements Serializable {

    @Serial
    private static final long serialVersionUID = 3140930925599382735L;

    private String descricao;
    @Enumerated(EnumType.STRING)
    private CategoriaType categoriaType;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        CategoriaId that = (CategoriaId) o;
        return descricao != null && Objects.equals(descricao, that.descricao)
                && categoriaType != null && Objects.equals(categoriaType, that.categoriaType);
    }

    @Override
    public int hashCode() {
        return Objects.hash(descricao, categoriaType);
    }
}