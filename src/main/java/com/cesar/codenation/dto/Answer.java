package com.cesar.codenation.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@AllArgsConstructor
@Builder
public class Answer {
	private int numero_casas;
	private String token;
	private String cifrado;
	private String decifrado;
	private String resumo_criptografico;
}
