package com.spoti.api.auth.domain.token.exception;

import com.spoti.api.auth.domain.error.ErrorCode;
import com.spoti.api.auth.domain.error.authException.CustomAuthException;

public class InvalidTokenException extends CustomAuthException {

	public InvalidTokenException() {
		super(ErrorCode.JWT_INVALID);
	}
}
