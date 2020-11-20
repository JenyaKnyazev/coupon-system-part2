package com.spring.CouponSystempart3.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;

import com.spring.CouponSystempart3.LoginManager;
import com.spring.CouponSystempart3.TokensManager;
import com.spring.CouponSystempart3.beans.Category;
import com.spring.CouponSystempart3.beans.ClientType;
import com.spring.CouponSystempart3.beans.Company;
import com.spring.CouponSystempart3.beans.Coupon;
import com.spring.CouponSystempart3.beans.Customer;
import com.spring.CouponSystempart3.beans.LoginResult;
import com.spring.CouponSystempart3.facade.ClientFacade;
import com.spring.CouponSystempart3.facade.CompanyFacade;
import com.spring.CouponSystempart3.facade.CustomerFacade;

@RestController
public class CustomerController extends ClientController {
	@Autowired
	private LoginManager loginManager;
	@Autowired
	private TokensManager tokensManager;

	@Override
	@GetMapping("api/customer/login/{email}/{password}")
	public ResponseEntity<LoginResult> login(@PathVariable("email") String email,
			@PathVariable("password") String password) {
		ClientFacade facade = loginManager.login(email, password, ClientType.Customer);

		boolean isLogged = facade != null;

		LoginResult result = new LoginResult();
		result.setLogged(isLogged);
		if (isLogged) {
			result.setToken(tokensManager.generateToken(facade, ClientType.Customer));
		}

		return ResponseEntity.ok().body(result);
	}

	@GetMapping("api/customer/coupon/{couponId}")
	public ResponseEntity<Void> purchaseCoupon(@PathVariable("couponId") int couponId, @RequestHeader("token") String token) {
		try {
			ClientFacade facade = tokensManager.getFacadeByToken(token);

			if (facade == null || !(facade instanceof CustomerFacade)) {
				return new ResponseEntity<Void>(HttpStatus.UNAUTHORIZED);
			}

			CustomerFacade customerFacade = (CustomerFacade) facade;

			customerFacade.purchaseCoupon(couponId);
		} catch (Exception ex) {
			return new ResponseEntity<Void>(HttpStatus.INTERNAL_SERVER_ERROR);
		}

		return new ResponseEntity<Void>(HttpStatus.OK);
	}

	@GetMapping("api/customer/coupon")
	public ResponseEntity<List<Coupon>> getCustomerCoupons(@RequestHeader("token") String token) {
		List<Coupon> coupons = null;

		try {
			ClientFacade facade = tokensManager.getFacadeByToken(token);

			if (facade == null || !(facade instanceof CustomerFacade)) {
				return new ResponseEntity<List<Coupon>>(HttpStatus.UNAUTHORIZED);
			}

			CustomerFacade customerFacade = (CustomerFacade) facade;

			coupons = customerFacade.getCustomerCoupons();
		} catch (Exception ex) {
			return new ResponseEntity<List<Coupon>>(HttpStatus.INTERNAL_SERVER_ERROR);
		}

		return new ResponseEntity<List<Coupon>>(coupons, HttpStatus.OK);
	}

	@GetMapping("api/customer/coupon/category/{category}")
	public ResponseEntity<List<Coupon>> getCustomerCouponsByCategory(@PathVariable("category") Category category,
			@RequestHeader("token") String token) {
		List<Coupon> coupons = null;

		try {
			ClientFacade facade = tokensManager.getFacadeByToken(token);

			if (facade == null || !(facade instanceof CustomerFacade)) {
				return new ResponseEntity<List<Coupon>>(HttpStatus.UNAUTHORIZED);
			}

			CustomerFacade customerFacade = (CustomerFacade) facade;

			coupons = customerFacade.getCustomerCoupons(category);
		} catch (Exception ex) {
			return new ResponseEntity<List<Coupon>>(HttpStatus.INTERNAL_SERVER_ERROR);
		}

		return new ResponseEntity<List<Coupon>>(coupons, HttpStatus.OK);
	}

	@GetMapping("api/customer/coupon/maxprice/{maxprice}")
	public ResponseEntity<List<Coupon>> getCustomerCouponsByMaxPrice(@PathVariable("maxprice") double maxPrice,
			@RequestHeader("token") String token) {
		List<Coupon> coupons = null;

		try {
			ClientFacade facade = tokensManager.getFacadeByToken(token);

			if (facade == null || !(facade instanceof CustomerFacade)) {
				return new ResponseEntity<List<Coupon>>(HttpStatus.UNAUTHORIZED);
			}

			CustomerFacade customerFacade = (CustomerFacade) facade;

			coupons = customerFacade.getCustomerCoupons(maxPrice);
		} catch (Exception ex) {
			return new ResponseEntity<List<Coupon>>(HttpStatus.INTERNAL_SERVER_ERROR);
		}

		return new ResponseEntity<List<Coupon>>(coupons, HttpStatus.OK);
	}

	@GetMapping("api/customer/details")
	public ResponseEntity<Customer> getCustomerDetails(@RequestHeader("token") String token) {
		Customer customer = null;

		try {
			ClientFacade facade = tokensManager.getFacadeByToken(token);

			if (facade == null || !(facade instanceof CustomerFacade)) {
				return new ResponseEntity<Customer>(HttpStatus.UNAUTHORIZED);
			}

			CustomerFacade customerFacade = (CustomerFacade) facade;

			customer = customerFacade.getCustomerDetails();
		} catch (Exception ex) {
			return new ResponseEntity<Customer>(HttpStatus.INTERNAL_SERVER_ERROR);
		}

		return new ResponseEntity<Customer>(customer, HttpStatus.OK);
	}

	@Override
	@GetMapping("api/customer/logout")
	public ResponseEntity<Void> logout(@RequestHeader("token") String token) {
		boolean isDeleted = tokensManager.deleteToken(token, ClientType.Customer);

		if (isDeleted) {
			return new ResponseEntity<Void>(HttpStatus.OK);
		} else {
			return new ResponseEntity<Void>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
}
