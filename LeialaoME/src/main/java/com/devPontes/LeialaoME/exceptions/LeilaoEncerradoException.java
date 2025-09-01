package com.devPontes.LeialaoME.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.CONFLICT)
public class LeilaoEncerradoException extends RuntimeException {
	private static final long serialVersionUID = 1L;

	public LeilaoEncerradoException(String message) {
		super(message);
	}

}
