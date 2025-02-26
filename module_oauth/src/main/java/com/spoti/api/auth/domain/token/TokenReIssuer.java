package com.spoti.api.auth.domain.token;

import org.springframework.stereotype.Service;
import com.spoti.api.auth.domain.token.TokenDto;

//인터페이스로 다시 구현 예정
@Service
public class TokenReIssuer {

	public TokenDto reissueAccessToken(String refreshToken) {
		// 토큰 재발급 로직 구현
		// 실제 구현은 프로젝트 요구사항에 맞게 작성하세요
		return TokenDto.builder()
			.accessToken("new-access-token")
			.refreshToken(refreshToken)
			.build();
	}
}
