package com.example.web;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@Tag(name = "테스트", description = "테스트 API")
public class TestController {

	@Operation(summary = "테스트 엔드포인트", description = "Swagger 작동 테스트용 엔드포인트")
	@GetMapping("/test")
	public String test() {
		return "테스트 성공!";
	}
}
