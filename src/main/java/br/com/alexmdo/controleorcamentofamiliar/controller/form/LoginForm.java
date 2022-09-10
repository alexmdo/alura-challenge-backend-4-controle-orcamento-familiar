package br.com.alexmdo.controleorcamentofamiliar.controller.form;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;

public record LoginForm(String email, String password) {

    public Authentication adapt() {
        return new UsernamePasswordAuthenticationToken(this.email, this.password);
    }

}
