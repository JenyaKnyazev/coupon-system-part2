package com.spring.CouponSystempart3.facade;

import java.util.List;
import java.util.Optional;
import java.util.ArrayList;

import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import com.spring.CouponSystempart3.beans.Company;
import com.spring.CouponSystempart3.beans.Coupon;
import com.spring.CouponSystempart3.beans.Customer;
import com.spring.CouponSystempart3.exceptions.ValidationException;

@Service
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class AdminFacade extends ClientFacade {
	private final static String EMAIL = "admin@admin.com";
	private final static String PASSWORD = "admin";

	public boolean login(String email, String password) {
		return email.equals(EMAIL) && password.equals(PASSWORD);
	}

	public void addCompany(Company company) throws ValidationException {
		List<Company> list = companiesDAO.findAll();
		for (Company i : list)
			if (i.getEmail().equals(company.getEmail()) || i.getName().equals(company.getName()))
				throw new ValidationException("Email or name alredy exist");

		companiesDAO.save(company);
	}

	public void updateCompany(Company company) {
		Company existingCompany = companiesDAO.getOne(company.getId());
		if (existingCompany == null)
			return;
		company.setName(existingCompany.getName());

		companiesDAO.save(company);
	}

	public void deleteCompany(int companyID) throws ValidationException {
		Company company = companiesDAO.getOne(companyID);
		if (company == null)
			throw new ValidationException("Company id wrong");
		
		List<Coupon> couponsToDelete = company.getCoupons();
	
		companiesDAO.deleteById(companyID);
		
		if (couponsToDelete != null) {
			for (Coupon i : couponsToDelete) {
				couponsDAO.deleteCouponPurchasesByCoupounId(i.getId());
				couponsDAO.deleteById(i.getId());
			}
		}
	}

	public List<Company> getAllCompanies() {
		return companiesDAO.findAll();
	}

	public Company getOneCompany(int companyID) {
		Optional<Company > c =	companiesDAO.findById(companyID);
		
		return c.isPresent() ?c.get():null;
	}

	public void addCustomer(Customer customer) throws ValidationException {
		List<Customer> list = customersDAO.findAll();
		for (Customer i : list)
			if (i.getEmail().equals(customer.getEmail()))
				throw new ValidationException("Alredy have a customer with that email");
		customersDAO.save(customer);
	}

	public void updateCustomer(Customer customer) {
		customersDAO.save(customer);
	}

	public void deleteCustomer(int customerID) {
		couponsDAO.deleteCouponPurchasesByCustomerId(customerID);
		customersDAO.deleteById(customerID);
	}

	public List<Customer> getAllCustomers() {
		return customersDAO.findAll();
	}

	public Customer getOneCustomer(int customerID) {
		Optional<Customer> c = customersDAO.findById(customerID);
		return c.isPresent() ? c.get() : null;
	}
}
