package com.spring.CouponSystempart2.dao;

import java.util.ArrayList;

import org.springframework.data.jpa.repository.JpaRepository;

import com.spring.CouponSystempart2.beans.Customer;

public interface CustomersDAO extends JpaRepository<Customer, Integer>{
	boolean existsByEmailAndPassword(String email,String password);
}
