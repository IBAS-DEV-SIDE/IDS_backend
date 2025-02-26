package com.spoti.api.auth.domain.oauth2;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;

import org.springframework.http.*;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;
import org.springframework.beans.factory.annotation.Value;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/auth")
@Tag(name = "Spotify OAuth", description = "Spotify OAuth 인증 관련 API")
public class OAuthController {

	@Value("${spotify.client-id}")
	private String clientId;

	@Value("${spotify.client-secret}")
	private String clientSecret;

	@Value("${spotify.redirect-uri}")
	private String redirectUri;

	@Value("${spotify.scope}")
	private String scope;

	@Value("${spotify.authorization-uri}")
	private String authorizationUri;

	@Value("${spotify.token-uri}")
	private String tokenUri;

	@Value("${spotify.user-info-uri}")
	private String userInfoUri;

	private final RestTemplate restTemplate = new RestTemplate();

	@Operation(summary = "Spotify 로그인 페이지로 리다이렉트", description = "사용자를 Spotify 로그인 페이지로 리다이렉트합니다.")
	@ApiResponses({
		@ApiResponse(responseCode = "302", description = "Spotify 로그인 페이지로 리다이렉트 성공")
	})
	@GetMapping("/login")
	public ResponseEntity<String> login() {
		String authUrl = UriComponentsBuilder.fromHttpUrl(authorizationUri)
			.queryParam("response_type", "code")
			.queryParam("client_id", clientId)
			.queryParam("scope", scope)
			.queryParam("redirect_uri", redirectUri)
			.queryParam("state", "random_state_string")
			.toUriString();

		HttpHeaders headers = new HttpHeaders();
		headers.add("Location", authUrl);
		return new ResponseEntity<>(headers, HttpStatus.FOUND);
	}

	@Operation(summary = "Spotify 인증 콜백 처리", description = "Spotify 인증 후 콜백을 처리하고 사용자 정보를 반환합니다.")
	@ApiResponses({
		@ApiResponse(responseCode = "200", description = "인증 성공 및 사용자 정보 반환"),
		@ApiResponse(responseCode = "400", description = "잘못된 요청"),
		@ApiResponse(responseCode = "401", description = "인증 실패")
	})

	//callback → Authorization Code 수신 후 Access Token 요청
	@GetMapping("/callback")
	public ResponseEntity<String> callback(@RequestParam("code") String code) {
		Map<String, String> requestBody = new HashMap<>();
		requestBody.put("grant_type", "authorization_code");
		requestBody.put("code", code);
		requestBody.put("redirect_uri", redirectUri);
		requestBody.put("client_id", clientId);
		requestBody.put("client_secret", clientSecret);

		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

		HttpEntity<Map<String, String>> request = new HttpEntity<>(requestBody, headers);
		ResponseEntity<Map> response = restTemplate.exchange(tokenUri, HttpMethod.POST, request, Map.class);

		if (response.getStatusCode() == HttpStatus.OK) {
			String accessToken = (String) response.getBody().get("access_token");
			return getUserInfo(accessToken);
		} else {
			return ResponseEntity.status(response.getStatusCode()).body("Error retrieving access token");
		}
	}

	//Access Token을 이용해 사용자 정보 가져오기
	private ResponseEntity<String> getUserInfo(String accessToken) {
		HttpHeaders headers = new HttpHeaders();
		headers.setBearerAuth(accessToken);

		HttpEntity<String> entity = new HttpEntity<>(headers);
		ResponseEntity<String> response = restTemplate.exchange(userInfoUri, HttpMethod.GET, entity, String.class);

		return ResponseEntity.ok(response.getBody());
	}
}
