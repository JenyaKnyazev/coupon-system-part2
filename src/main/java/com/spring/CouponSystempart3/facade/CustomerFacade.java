package com.spring.CouponSystempart3.facade;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import com.spring.CouponSystempart3.exceptions.CouponAlredyExsitsException;
import com.spring.CouponSystempart3.exceptions.CouponExpiredException;
import com.spring.CouponSystempart3.exceptions.CouponOutOfStockException;
import com.spring.CouponSystempart3.beans.Category;
import com.spring.CouponSystempart3.beans.Company;
import com.spring.CouponSystempart3.beans.Coupon;
import com.spring.CouponSystempart3.beans.Customer;

@Service
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class CustomerFacade extends ClientFacade {
	private int customerID;

	@Override
	public boolean login(String email, String password) {
		boolean res = customersDAO.existsByEmailAndPassword(email, password);
		if (res) {
			List<Customer> customers = customersDAO.findAll();
			for (Customer i : customers)
				if (i.getEmail().equals(email) && i.getPassword().equals(password)) {
					customerID = i.getId();
					break;
				}
		}
		return res;
	}

	private boolean checkIfCouponPurchesed(Coupon coupon) {
		Customer customer = customersDAO.getOne(customerID);

		if (customer == null) {
			return false;
		}

		if (customer.getCoupons() != null) {
			for (Coupon i : customer.getCoupons())
				if (i.getId() == coupon.getId())
					return true;
		}
		return false;
	}

	public void purchaseCoupon(int couponId) 
			throws CouponOutOfStockException, CouponExpiredException, CouponAlredyExsitsException{
		Coupon coupon = couponsDAO.getOne(couponId);
		purchaseCoupon(coupon);
	}
	
	public void purchaseCoupon(Coupon coupon)
			throws CouponOutOfStockException, CouponExpiredException, CouponAlredyExsitsException {
		Date now = new Date();
		if (coupon.getAmount() == 0)
			throw new CouponOutOfStockException();

		if (coupon.getEndDate().before(now))
			throw new CouponExpiredException();

		if (checkIfCouponPurchesed(coupon))
			throw new CouponAlredyExsitsException();

		couponsDAO.addCouponPurchase(customerID, coupon.getId());
		coupon.setAmount(coupon.getAmount() - 1);
		couponsDAO.save(coupon);
	}

	public List<Coupon> getCustomerCoupons() {
		Customer customer = customersDAO.getOne(customerID);

		if (customer == null || customer.getCoupons() == null)
			return Collections.emptyList();

		return customer.getCoupons();
	}

	public ArrayList<Coupon> getCustomerCoupons(Category category) {
		List<Coupon> coupons = getCustomerCoupons();

		ArrayList<Coupon> results = new ArrayList<Coupon>();

		for (Coupon coupon : coupons) {
			if (coupon.getCategory() == category) {
				results.add(coupon);
			}
		}

		return results;
	}

	public ArrayList<Coupon> getCustomerCoupons(double maxPrice) {
		List<Coupon> coupons = getCustomerCoupons();

		ArrayList<Coupon> results = new ArrayList<Coupon>();

		for (Coupon coupon : coupons) {
			if (coupon.getPrice() <= maxPrice) {
				results.add(coupon);
			}
		}

		return results;
	}

	public Customer getCustomerDetails() {
		Optional<Customer> c = customersDAO.findById(customerID);
		
		return c.isPresent() ? c.get() : null;
	}
}
