package com.spring.CouponSystempart2.facade;

import org.springframework.beans.factory.annotation.Autowired;

import com.spring.CouponSystempart2.dao.CompaniesDAO;
import com.spring.CouponSystempart2.dao.CouponsDAO;
import com.spring.CouponSystempart2.dao.CustomersDAO;

public abstract class ClientFacade {
	@Autowired
	protected CompaniesDAO companiesDAO;
	@Autowired
	protected CouponsDAO couponsDAO;
	@Autowired
	protected CustomersDAO customersDAO;
	public abstract boolean login(String email,String password); 
	
}
