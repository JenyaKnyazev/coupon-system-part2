package com.spring.CouponSystempart2.facade;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import com.spring.CouponSystempart2.beans.Category;
import com.spring.CouponSystempart2.beans.Company;
import com.spring.CouponSystempart2.beans.Coupon;
import com.spring.CouponSystempart2.exceptions.ValidationException;


@Service
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class CompanyFacade extends ClientFacade {
	private int companyID;

	@Override
	public boolean login(String email, String password) {
		boolean res = companiesDAO.existsByEmailAndPassword(email, password);
		if (res) {
			List<Company> companies = companiesDAO.findAll();
			for (Company i : companies)
				if (i.getEmail().equals(email) && i.getPassword().equals(password)) {
					companyID = i.getId();
					break;
				}
		}
		return res;
	}

	public void addCoupon(Coupon coupon) throws ValidationException {
		List<Coupon> coupons = couponsDAO.findAll();
		for (Coupon c : coupons) {
			if (c.getCompanyId() == coupon.getCompanyId() && 
				c.getTitle() != null && 
				coupon.getTitle() != null && 
				c.getTitle().equals(coupon.getTitle())) {
				throw new ValidationException("cannot add coupon with the same title");
			}
		}
		coupon.setCompanyId(companyID);
		couponsDAO.save(coupon);
	}

	public void updateCoupon(Coupon coupon) {
		coupon.setCompanyId(companyID);
		couponsDAO.save(coupon);
	}

	public void deleteCoupon(int couponID) {
		couponsDAO.deleteCouponPurchasesByCoupounId(couponID);
		couponsDAO.deleteById(couponID);
	}

	public ArrayList<Coupon> getCompanyCoupons() {
		List<Coupon> coupons = couponsDAO.findAll();

		ArrayList<Coupon> results = new ArrayList<Coupon>();

		for (Coupon c : coupons) {
			if (c.getCompanyId() == companyID) {
				results.add(c);
			}
		}

		return results;
	}

	public ArrayList<Coupon> getCompanyCoupons(Category category) {
		ArrayList<Coupon> coupons = getCompanyCoupons();
		ArrayList<Coupon> result = new ArrayList<>();
		for (Coupon i : coupons)
			if (i.getCategory() == category)
				result.add(i);
		return result;
	}

	public ArrayList<Coupon> getCompanyCoupons(double maxPrice) {
		ArrayList<Coupon> coupons = getCompanyCoupons();
		ArrayList<Coupon> result = new ArrayList<>();
		for (Coupon i : coupons)
			if (i.getPrice() <= maxPrice)
				result.add(i);
		return result;
	}

	public Company getCompanyDetails() {
		return companiesDAO.getOne(companyID);
	}

}
