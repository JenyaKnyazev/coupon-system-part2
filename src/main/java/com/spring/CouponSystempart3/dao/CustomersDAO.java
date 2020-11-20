package com.spring.CouponSystempart3.dao;

import java.util.ArrayList;

import org.springframework.data.jpa.repository.JpaRepository;

import com.spring.CouponSystempart3.beans.Customer;

public interface CustomersDAO extends JpaRepository<Customer, Integer>{
	boolean existsByEmailAndPassword(String email,String password);
}
