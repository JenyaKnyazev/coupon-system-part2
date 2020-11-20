package com.spring.CouponSystempart3.beans;

import java.util.Date;

import com.spring.CouponSystempart3.facade.ClientFacade;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class TokenValue {
	private Date timestamp;
	private String token;
	private ClientType clientType;
	private ClientFacade clientFacade;
}
