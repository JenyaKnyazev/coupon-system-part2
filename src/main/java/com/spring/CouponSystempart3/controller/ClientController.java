package com.spring.CouponSystempart3.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;

import com.spring.CouponSystempart3.beans.ClientType;
import com.spring.CouponSystempart3.beans.LoginResult;
import com.spring.CouponSystempart3.exceptions.ValidationException;

public abstract class ClientController {
	public abstract ResponseEntity<LoginResult> login(String email, String password);

	public abstract ResponseEntity<Void> logout(String token);
	
//    /**
//     * Handles ResourceExceptions for the SpringMVC controllers.
//     * @param e SpringMVC controller exception.
//     * @return http response entity
//     * @see ExceptionHandler
//     */
//    @ExceptionHandler(ValidationException.class)
//    public ResponseEntity<String> handleException(ValidationException e) {
//        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
//    }

}
