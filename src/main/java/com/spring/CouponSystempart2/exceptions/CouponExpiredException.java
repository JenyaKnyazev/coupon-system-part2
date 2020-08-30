package com.spring.CouponSystempart2.exceptions;

public class CouponExpiredException extends Exception{
	public CouponExpiredException() {
		super("Coupon date is expired");
	}
}
