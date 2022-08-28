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
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    private String descricao;

    @Enumerated(EnumType.STRING)
    private CategoriaType categoriaType;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        Categoria categoria = (Categoria) o;
        return id != null && Objects.equals(id, categoria.id);
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}