package com.spring.CouponSystempart2.exceptions;

public class CouponOutOfStockException extends Exception{
	public CouponOutOfStockException() {
		super("Coupon is out of stock");
	}
}
