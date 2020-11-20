package com.spring.CouponSystempart3.job;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.spring.CouponSystempart3.dao.CouponsDAO;
import com.spring.CouponSystempart3.beans.Coupon;

@Component
public class CouponExpirationDailyJob implements Runnable {
	@Autowired
	private CouponsDAO couponsDAO;

	private boolean quit;
	private final static int JOB_INTERVAL_IN_MILISECONDS = 1000 * 60 * 60 * 24;

	public CouponExpirationDailyJob() {
		quit = false;
	}

	@Override
	public void run() {
		while (!quit) {
			try {
				runJob();
				Thread.sleep(JOB_INTERVAL_IN_MILISECONDS);
			} catch (Exception e) {
				System.out.println("Failed to run daily job");
				e.printStackTrace();
			}
		}

	}

	public void stop() {
		quit = true;
	}

	private void runJob() {
		List<Coupon> coupons = couponsDAO.findAll();
		List<Coupon> expiredCoupons = findExpiredCoupons(coupons, new Date());

		for (Coupon i : expiredCoupons) {
			couponsDAO.deleteCouponPurchasesByCoupounId(i.getId());
			couponsDAO.deleteById(i.getId());
		}
	}

	private List<Coupon> findExpiredCoupons(List<Coupon> coupons, Date date) {
		ArrayList<Coupon> expiredCoupons = new ArrayList<>();
		for (Coupon i : coupons)
			if (i.getEndDate().before(date))
				expiredCoupons.add(i);
		return expiredCoupons;
	}

}
