package br.com.alexmdo.controleorcamentofamiliar.exception;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

@AllArgsConstructor
@Getter
@ToString
@EqualsAndHashCode
public class FormErrorDTO {

    private String field;
    private String error;

}
