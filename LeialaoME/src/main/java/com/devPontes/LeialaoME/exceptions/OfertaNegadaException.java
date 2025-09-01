package com.devPontes.LeialaoME.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.CONFLICT)
public class OfertaNegadaException extends RuntimeException {
	private static final long serialVersionUID = 1L;

	public OfertaNegadaException(String message) {
		super(message);
	}

}
