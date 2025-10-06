package com.devPontes.LeialaoME.exceptions.handler;

import java.util.Date;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.WebRequest;

import com.devPontes.LeialaoME.exceptions.ExceptionResponse;
import com.devPontes.LeialaoME.exceptions.JWTCreationException;
import com.devPontes.LeialaoME.exceptions.LeilaoEncerradoException;
import com.devPontes.LeialaoME.exceptions.LeilaoException;
import com.devPontes.LeialaoME.exceptions.OfertaNegadaException;
import com.devPontes.LeialaoME.exceptions.UsuarioNaoEncontradoException;

@ControllerAdvice
@RestController
public class CustomExceptionHandler {

	@ExceptionHandler(Exception.class)
	ResponseEntity<ExceptionResponse> handleAllExcpetions(Exception ex, WebRequest request) {
		ExceptionResponse response = new ExceptionResponse(new Date(), ex.getMessage(), request.getDescription(false));
		return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
	}

	@ExceptionHandler(LeilaoEncerradoException.class)
	public ResponseEntity<ExceptionResponse> handleLeilaoEncerrado(LeilaoEncerradoException ex, WebRequest request) {
		ExceptionResponse erro = new ExceptionResponse(new Date(), ex.getMessage(), request.getDescription(false));
		return new ResponseEntity<>(erro, HttpStatus.CONFLICT);
	}

	// Tratar oferta inválida / NEGADA
	@ExceptionHandler(OfertaNegadaException.class)
	public ResponseEntity<ExceptionResponse> handleOfertaEncerrado(OfertaNegadaException ex, WebRequest request) {
		ExceptionResponse erro = new ExceptionResponse(new Date(), ex.getMessage(), request.getDescription(false));
		return new ResponseEntity<>(erro, HttpStatus.CONFLICT);
	}

	@ExceptionHandler(UsuarioNaoEncontradoException.class)
	public ResponseEntity<ExceptionResponse> handleUsuarioNaoEncontrado(UsuarioNaoEncontradoException ex,
			WebRequest request) {
		ExceptionResponse erro = new ExceptionResponse(new Date(), ex.getMessage(), request.getDescription(false));
		return new ResponseEntity<>(erro, HttpStatus.NOT_FOUND);
	}

	@ExceptionHandler(JWTCreationException.class)
	public ResponseEntity<ExceptionResponse> handJwtCreatErrro(JWTCreationException ex, WebRequest request) {
		ExceptionResponse erro = new ExceptionResponse(new Date(), ex.getMessage(), request.getDescription(false));
		return new ResponseEntity<>(erro, HttpStatus.CONFLICT);
	}
	
	
	@ExceptionHandler(LeilaoException.class)
	public ResponseEntity<ExceptionResponse> handLeilaoExcpetions(LeilaoException ex, WebRequest request) {
		ExceptionResponse erro = new ExceptionResponse(new Date(), ex.getMessage(), request.getDescription(false));
		return new ResponseEntity<>(erro, HttpStatus.CONFLICT);
	}
	
	

}
