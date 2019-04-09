package com.cesar.codenation.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@AllArgsConstructor
@Builder
public class Answer {
	@JsonProperty("numero_casas")
	private int numeroCasas;
	private String token;
	private String cifrado;
	private String decifrado;
	@JsonProperty("resumo_criptografico")
	private String resumoCriptografico;
}
