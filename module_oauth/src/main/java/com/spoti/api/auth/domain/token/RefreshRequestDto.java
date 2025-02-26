package com.spoti.api.auth.domain.token;

import lombok.*;

@Data
public class RefreshRequestDto {
	private String refreshToken;
}
