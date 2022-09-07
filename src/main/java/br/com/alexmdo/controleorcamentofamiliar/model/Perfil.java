package br.com.alexmdo.controleorcamentofamiliar.model;

import lombok.*;
import org.springframework.security.core.GrantedAuthority;

import javax.persistence.*;
import java.util.Objects;

@Entity
@Table(name = "perfis")
@ToString
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Perfil implements GrantedAuthority {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String nome;

    @Override
    public String getAuthority() {
        return nome;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Perfil perfil = (Perfil) o;
        return id.equals(perfil.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

}