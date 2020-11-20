package com.spring.CouponSystempart3.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
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
import com.spring.CouponSystempart3.exceptions.ValidationException;
import com.spring.CouponSystempart3.facade.AdminFacade;
import com.spring.CouponSystempart3.facade.ClientFacade;
import com.spring.CouponSystempart3.facade.CompanyFacade;

@RestController
public class CompanyController extends ClientController {
	@Autowired
	private LoginManager loginManager;
	@Autowired
	private TokensManager tokensManager;

	@Override
	@GetMapping("api/company/login/{email}/{password}")
	public ResponseEntity<LoginResult> login(@PathVariable("email") String email,
			@PathVariable("password") String password) {
		ClientFacade facade = loginManager.login(email, password, ClientType.Company);

		boolean isLogged = facade != null;

		LoginResult result = new LoginResult();
		result.setLogged(isLogged);
		if (isLogged) {
			result.setToken(tokensManager.generateToken(facade, ClientType.Company));
		}

		return ResponseEntity.ok().body(result);
	}

	@PostMapping("api/company/copuon")
	public ResponseEntity<Void> addCoupon(@RequestBody Coupon coupon, @RequestHeader("token") String token) {
		try {
			ClientFacade facade = tokensManager.getFacadeByToken(token);

			if (facade == null || !(facade instanceof CompanyFacade)) {
				return new ResponseEntity<Void>(HttpStatus.UNAUTHORIZED);
			}

			CompanyFacade companyFacade = (CompanyFacade) facade;

			companyFacade.addCoupon(coupon);
		} catch (ValidationException ex) {
			return new ResponseEntity<Void>(HttpStatus.BAD_REQUEST);
		} catch (Exception ex) {
			return new ResponseEntity<Void>(HttpStatus.INTERNAL_SERVER_ERROR);
		}

		return new ResponseEntity<Void>(HttpStatus.OK);
	}

	@PutMapping("api/company/copuon")
	public ResponseEntity<Void> updateCoupon(@RequestBody Coupon coupon, @RequestHeader("token") String token) {
		try {
			ClientFacade facade = tokensManager.getFacadeByToken(token);

			if (facade == null || !(facade instanceof CompanyFacade)) {
				return new ResponseEntity<Void>(HttpStatus.UNAUTHORIZED);
			}

			CompanyFacade companyFacade = (CompanyFacade) facade;

			companyFacade.updateCoupon(coupon);
		} catch (Exception ex) {
			return new ResponseEntity<Void>(HttpStatus.INTERNAL_SERVER_ERROR);
		}

		return new ResponseEntity<Void>(HttpStatus.OK);
	}

	@DeleteMapping("api/company/coupon/{couponId}")
	public ResponseEntity<Void> deleteCoupon(@PathVariable("couponId") int couponId,
			@RequestHeader("token") String token) {
		try {
			ClientFacade facade = tokensManager.getFacadeByToken(token);

			if (facade == null || !(facade instanceof CompanyFacade)) {
				return new ResponseEntity<Void>(HttpStatus.UNAUTHORIZED);
			}

			CompanyFacade companyFacade = (CompanyFacade) facade;

			companyFacade.deleteCoupon(couponId);
		} catch (Exception ex) {
			return new ResponseEntity<Void>(HttpStatus.INTERNAL_SERVER_ERROR);
		}

		return new ResponseEntity<Void>(HttpStatus.OK);
	}

	@GetMapping("api/company/coupon")
	public ResponseEntity<List<Coupon>> getAllCompanyCoupons(@RequestHeader("token") String token) {
		List<Coupon> coupons = null;

		try {
			ClientFacade facade = tokensManager.getFacadeByToken(token);

			if (facade == null || !(facade instanceof CompanyFacade)) {
				return new ResponseEntity<List<Coupon>>(HttpStatus.UNAUTHORIZED);
			}

			CompanyFacade companyFacade = (CompanyFacade) facade;

			coupons = companyFacade.getCompanyCoupons();
		} catch (Exception ex) {
			return new ResponseEntity<List<Coupon>>(HttpStatus.INTERNAL_SERVER_ERROR);
		}

		return new ResponseEntity<List<Coupon>>(coupons, HttpStatus.OK);
	}

	@GetMapping("api/company/coupon/category/{category}")
	public ResponseEntity<List<Coupon>> getCompanyCouponsByCategory(@PathVariable("category") Category category,
			@RequestHeader("token") String token) {
		List<Coupon> coupons = null;

		try {
			ClientFacade facade = tokensManager.getFacadeByToken(token);

			if (facade == null || !(facade instanceof CompanyFacade)) {
				return new ResponseEntity<List<Coupon>>(HttpStatus.UNAUTHORIZED);
			}

			CompanyFacade companyFacade = (CompanyFacade) facade;

			coupons = companyFacade.getCompanyCoupons(category);
		} catch (Exception ex) {
			return new ResponseEntity<List<Coupon>>(HttpStatus.INTERNAL_SERVER_ERROR);
		}

		return new ResponseEntity<List<Coupon>>(coupons, HttpStatus.OK);
	}

	@GetMapping("api/company/coupon/maxprice/{maxPrice}")
	public ResponseEntity<List<Coupon>> getCompanyCouponsByMaxPrice(@PathVariable("maxPrice") double maxPrice,
			@RequestHeader("token") String token) {
		List<Coupon> coupons = null;

		try {
			ClientFacade facade = tokensManager.getFacadeByToken(token);

			if (facade == null || !(facade instanceof CompanyFacade)) {
				return new ResponseEntity<List<Coupon>>(HttpStatus.UNAUTHORIZED);
			}

			CompanyFacade companyFacade = (CompanyFacade) facade;

			coupons = companyFacade.getCompanyCoupons(maxPrice);
		} catch (Exception ex) {
			return new ResponseEntity<List<Coupon>>(HttpStatus.INTERNAL_SERVER_ERROR);
		}

		return new ResponseEntity<List<Coupon>>(coupons, HttpStatus.OK);
	}

	@GetMapping("api/company/details")
	public ResponseEntity<Company> getCompanyDetails(@RequestHeader("token") String token) {
		Company company = null;

		try {
			ClientFacade facade = tokensManager.getFacadeByToken(token);

			if (facade == null || !(facade instanceof CompanyFacade)) {
				return new ResponseEntity<Company>(HttpStatus.UNAUTHORIZED);
			}

			CompanyFacade companyFacade = (CompanyFacade) facade;

			company = companyFacade.getCompanyDetails();
		} catch (Exception ex) {
			return new ResponseEntity<Company>(HttpStatus.INTERNAL_SERVER_ERROR);
		}

		return new ResponseEntity<Company>(company, HttpStatus.OK);
	}

	@Override
	@GetMapping("api/company/logout")
	public ResponseEntity<Void> logout(@RequestHeader("token") String token) {
		boolean isDeleted = tokensManager.deleteToken(token, ClientType.Company);

		if (isDeleted) {
			return new ResponseEntity<Void>(HttpStatus.OK);
		} else {
			return new ResponseEntity<Void>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
}
