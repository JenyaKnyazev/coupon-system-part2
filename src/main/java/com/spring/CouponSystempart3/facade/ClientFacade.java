package com.spring.CouponSystempart3.facade;

import org.springframework.beans.factory.annotation.Autowired;

import com.spring.CouponSystempart3.dao.CompaniesDAO;
import com.spring.CouponSystempart3.dao.CouponsDAO;
import com.spring.CouponSystempart3.dao.CustomersDAO;

public abstract class ClientFacade {
	@Autowired
	protected CompaniesDAO companiesDAO;
	@Autowired
	protected CouponsDAO couponsDAO;
	@Autowired
	protected CustomersDAO customersDAO;
	public abstract boolean login(String email,String password); 
	
}
