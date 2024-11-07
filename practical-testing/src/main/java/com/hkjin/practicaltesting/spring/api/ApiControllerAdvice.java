package com.hkjin.practicaltesting.spring.api;

import org.springframework.http.HttpStatus;
import org.springframework.validation.BindException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class ApiControllerAdvice {

	/* @Valid 로 걸리면 BindException 이 터진다. */
	@ResponseStatus(HttpStatus.BAD_REQUEST)
	@ExceptionHandler(BindException.class)
	public ApiResponse<Object> bindException(BindException e) {

		return ApiResponse.of(HttpStatus.BAD_REQUEST,
			e.getBindingResult().getAllErrors().get(0).getDefaultMessage(),null
		);
	}
}
