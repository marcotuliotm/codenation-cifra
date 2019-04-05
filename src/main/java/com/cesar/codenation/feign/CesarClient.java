package com.cesar.codenation.feign;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import com.cesar.codenation.dto.Answer;

@FeignClient(url = "https://api.codenation.dev/v1/challenge/dev-ps", name = "cesarClient")
@Component
public interface CesarClient {

	@GetMapping("/generate-data")
	Answer generateData(@RequestParam("token") String token);

	@PostMapping(path = "/submit-solution", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
	void submitData(@RequestParam("token") String token, @RequestBody MultipartFile file);

}
