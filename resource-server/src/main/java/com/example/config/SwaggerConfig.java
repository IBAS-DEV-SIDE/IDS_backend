package com.example.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.http.HttpHeaders;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import io.swagger.v3.oas.models.servers.Server;
import org.springdoc.core.models.GroupedOpenApi;

@Configuration
public class SwaggerConfig {

	private static final String DOMAIN = "http://localhost:8080";

	private Info apiInfo() {
		return new Info()
			.title("IDS_backend API")
			.version("v1.0")
			.description("API 명세서");
	}

	private SecurityScheme securityScheme() {
		return new SecurityScheme()
			.type(SecurityScheme.Type.HTTP)
			.scheme("bearer")
			.bearerFormat("JWT")
			.in(SecurityScheme.In.HEADER)
			.name(HttpHeaders.AUTHORIZATION);
	}

	private SecurityRequirement securityRequirement() {
		SecurityRequirement requirement = new SecurityRequirement();
		requirement.addList("JWT");
		return requirement;
	}

	private OpenAPI createOpenAPI(
		Info info,
		SecurityScheme securityScheme,
		SecurityRequirement securityRequirement,
		Server... servers) {

		OpenAPI openAPI = new OpenAPI()
			.components(new Components().addSecuritySchemes("JWT", securityScheme))
			.addSecurityItem(securityRequirement)
			.info(info);

		for (Server server : servers) {
			openAPI.addServersItem(server);
		}

		return openAPI;
	}

	@Bean
	@Profile("dev") //추후에 실서버 발행하면 사용
	public OpenAPI devOpenAPI() {
		Server server = new Server().url(DOMAIN);
		return createOpenAPI(apiInfo(), securityScheme(), securityRequirement(), server);
	}

	@Bean
	@Profile("local")
	public OpenAPI localOpenAPI() {
		return createOpenAPI(apiInfo(), securityScheme(), securityRequirement());
	}

	//아래는 예시로 적어둔 것이고 추후에 수정
	@Bean
	public GroupedOpenApi getAllApi() {
		return GroupedOpenApi.builder()
			.group("전체 API")
			.pathsToMatch("/**")
			.build();
	}

	@Bean
	public GroupedOpenApi getSpotifyUserApi() {
		return GroupedOpenApi.builder()
			.group("사용자 관련")
			.pathsToMatch("/api/users/**", "/api/auth/**", "/api/profiles/**")
			.build();
	}

	@Bean
	public GroupedOpenApi getSpotifyMusicApi() {
		return GroupedOpenApi.builder()
			.group("음악 관련")
			.pathsToMatch("/api/tracks/**", "/api/albums/**", "/api/artists/**", "/api/playlists/**")
			.build();
	}

	@Bean
	public GroupedOpenApi getSpotifySearchApi() {
		return GroupedOpenApi.builder()
			.group("검색 관련")
			.pathsToMatch("/api/search/**")
			.build();
	}

	@Bean
	public GroupedOpenApi getBoardApi() {
		return GroupedOpenApi.builder()
			.group("게시판 관련")
			.pathsToMatch("/api/boards/**", "/api/posts/**", "/api/comments/**")
			.build();
	}

	@Bean
	public GroupedOpenApi getPlayerApi() {
		return GroupedOpenApi.builder()
			.group("플레이어 관련")
			.pathsToMatch("/api/player/**", "/api/queue/**")
			.build();
	}
}
