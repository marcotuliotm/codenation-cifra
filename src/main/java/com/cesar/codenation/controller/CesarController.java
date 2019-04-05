package com.cesar.codenation.controller;

import static org.springframework.http.ResponseEntity.*;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.cesar.codenation.dto.Answer;
import com.cesar.codenation.service.CesarService;

@RestController
@RequestMapping(value = "cesar", produces = MediaType.APPLICATION_JSON_VALUE)
public class CesarController {

	@Autowired
	private CesarService cesarService;

	@GetMapping("/{token}")
	public ResponseEntity<Answer> sendMessage(@PathVariable("token") String token) {
		try {
			return ok(cesarService.sendMessage(token));
		} catch (IOException e) {
			return ResponseEntity.badRequest().body(null);
		}
	}
}
