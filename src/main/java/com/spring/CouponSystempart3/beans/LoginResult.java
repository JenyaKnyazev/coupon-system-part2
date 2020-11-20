package com.spring.CouponSystempart3.beans;

import lombok.Data;

@Data
public class LoginResult {
	private boolean isLogged;
	private String token;
}
