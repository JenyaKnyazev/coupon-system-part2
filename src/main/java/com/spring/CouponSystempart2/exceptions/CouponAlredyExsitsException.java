package com.spring.CouponSystempart2.exceptions;

public class CouponAlredyExsitsException extends Exception{
	public CouponAlredyExsitsException() {
		super("Coupon alredy purchased");
	}
}
