package com.spring.CouponSystempart3.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.data.jpa.repository.Modifying;
import com.spring.CouponSystempart3.beans.Coupon;

public interface CouponsDAO extends JpaRepository<Coupon, Integer> {
	@Transactional
	@Modifying
	@Query(value = "INSERT INTO customer_coupons (coupons_id,customer_id) VALUES (:couponId,:customerId)", nativeQuery = true)
	void addCouponPurchase(@Param("customerId") int customerId, @Param("couponId") int couponId);

	@Transactional
	@Modifying
	@Query(value = "DELETE FROM customer_coupons WHERE coupons_id = :couponId", nativeQuery = true)
	void deleteCouponPurchasesByCoupounId(@Param("couponId") int couponId);

	@Transactional
	@Modifying
	@Query(value = "DELETE FROM customer_coupons WHERE customer_id = :customerId", nativeQuery = true)
	void deleteCouponPurchasesByCustomerId(@Param("customerId") int customerId);
}
