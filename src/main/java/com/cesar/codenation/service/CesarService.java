package com.cesar.codenation.service;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Files;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.commons.CommonsMultipartFile;

import com.cesar.codenation.dto.Answer;
import com.cesar.codenation.feign.CesarClient;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.base.Charsets;
import com.google.common.hash.Hashing;

@Service
public class CesarService {

	@Autowired
	private CesarClient cesarClient;

	public Answer sendMessage(String token) throws IOException {
		final Answer answer = cesarClient.generateData(token);

		answer.setDecifrado(decifra(answer.getCifrado(), answer.getNumero_casas()));

		final String resumo_criptografico = Hashing.sha1()
				.hashString(answer.getDecifrado(), Charsets.UTF_8)
				.toString();
		answer.setResumo_criptografico(resumo_criptografico);

		MultipartFile multipartFile = buildMultipartFile(answer);
		cesarClient.submitData(token, multipartFile);

		return answer;
	}

	private MultipartFile buildMultipartFile(Answer answer) throws IOException {
		ObjectMapper mapper = new ObjectMapper();
		final File file = new File("answer.json");
		mapper.writeValue(file, answer);
		FileItem fileItem = new DiskFileItemFactory().createItem("answer",
				Files.probeContentType(file.toPath()), false, file.getName());

		InputStream in = new FileInputStream(file);
		OutputStream out = fileItem.getOutputStream();
		in.transferTo(out);

		return new CommonsMultipartFile(fileItem);
	}

	private String decifra(final String cifrado, final int chave) {

		final StringBuilder decifrado = new StringBuilder();
		cifrado.chars().forEach(letra -> decifrado.append(mudaLetra(letra, chave)));

		return decifrado.toString();
	}

	private char mudaLetra(int letra, int chave) {
		if (letra < 97 || letra > 122) {
			return (char) letra;
		}

		letra -= chave;

		if (letra < 97) {
			letra = 122 - (-1 + (97 - letra));
		}
		return (char) letra;
	}
}
