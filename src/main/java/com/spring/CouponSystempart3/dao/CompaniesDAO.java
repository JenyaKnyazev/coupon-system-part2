package com.spring.CouponSystempart3.dao;

import org.springframework.data.jpa.repository.JpaRepository;

import com.spring.CouponSystempart3.beans.Company;

public interface CompaniesDAO extends JpaRepository<Company, Integer> {
	boolean existsByEmailAndPassword(String email, String password);
	
}
