package br.com.alexmdo.controleorcamentofamiliar.controller.form;

import lombok.Data;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;

@Data
public class LoginForm {

    private final String email;
    private final String password;

    public Authentication convert() {
        return new UsernamePasswordAuthenticationToken(this.email, this.password);
    }

}
