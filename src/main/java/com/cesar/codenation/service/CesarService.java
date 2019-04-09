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

		final String decifrado = decifra(answer.getCifrado(), answer.getNumeroCasas());
		answer.setDecifrado(decifrado);

		final String resumoCriptografico = Hashing.sha1()
				.hashString(answer.getDecifrado(), Charsets.UTF_8)
				.toString();
		answer.setResumoCriptografico(resumoCriptografico);

		MultipartFile multipartFile = buildMultipartFile(answer);
		cesarClient.submitData(token, multipartFile);

		return answer;
	}

	private MultipartFile buildMultipartFile(final Answer answer) throws IOException {
		final ObjectMapper mapper = new ObjectMapper();
		final File file = File.createTempFile("answer", ".json");
		mapper.writeValue(file, answer);
		final FileItem fileItem = new DiskFileItemFactory().createItem("answer",
				Files.probeContentType(file.toPath()), false, file.getName());

		final InputStream in = new FileInputStream(file);
		final OutputStream out = fileItem.getOutputStream();
		in.transferTo(out);

		return new CommonsMultipartFile(fileItem);
	}

	private String decifra(final String cifrado, final int chave) {
		final StringBuilder decifrado = new StringBuilder();
		cifrado.chars().forEach(letra -> decifrado.append(mudaLetra(letra, chave)));

		return decifrado.toString();
	}

	private char mudaLetra(int letra, final int chave) {
		if (letra < 97 || letra > 122) {
			return (char) letra;
		}

		letra -= chave;

		if (letra < 97) {
			letra = 122 - (96 - letra);
		}
		return (char) letra;
	}
}
