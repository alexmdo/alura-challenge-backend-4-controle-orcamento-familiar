package br.com.alexmdo.controleorcamentofamiliar.security;

import br.com.alexmdo.controleorcamentofamiliar.model.User;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
public class TokenService {


    @Value("${orcamento.jwt.expiration}")
    private long expirationInMillis;
    @Value("${orcamento.jwt.secret}")
    private String secret;

    public String gerarToken(Authentication authenticate) {
        User principal = (User) authenticate.getPrincipal();
        Date now = new Date();
        Date exp = new Date(now.getTime() + expirationInMillis);
        return Jwts.builder()
                .setIssuer("API Controle Orcamento Familiar")
                .setSubject(principal.getId().toString())
                .setIssuedAt(now)
                .setExpiration(exp)
                .signWith(SignatureAlgorithm.HS256, secret)
                .compact();
    }

    public boolean isTokenValido(String token) {
        try {
            Jwts.parser().setSigningKey(this.secret).parseClaimsJws(token);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public Long getIdUsuario(String token) {
        Claims body = Jwts.parser().setSigningKey(this.secret).parseClaimsJws(token).getBody();
        return Long.valueOf(body.getSubject());
    }
}
