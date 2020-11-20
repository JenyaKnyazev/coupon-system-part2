package com.spring.CouponSystempart3.exceptions;

public class CouponAlredyExsitsException extends Exception{
	public CouponAlredyExsitsException() {
		super("Coupon alredy purchased");
	}
}
