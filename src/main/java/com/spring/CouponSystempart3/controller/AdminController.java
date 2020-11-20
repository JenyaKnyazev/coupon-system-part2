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
import com.spring.CouponSystempart3.beans.ClientType;
import com.spring.CouponSystempart3.beans.Company;
import com.spring.CouponSystempart3.beans.Customer;
import com.spring.CouponSystempart3.beans.LoginResult;
import com.spring.CouponSystempart3.exceptions.ValidationException;
import com.spring.CouponSystempart3.facade.AdminFacade;
import com.spring.CouponSystempart3.facade.ClientFacade;

@RestController
public class AdminController extends ClientController {
	@Autowired
	private LoginManager loginManager;
	@Autowired
	private TokensManager tokensManager;

	@Override
	@GetMapping("api/admin/login/{email}/{password}")
	public ResponseEntity<LoginResult> login(@PathVariable("email") String email,
			@PathVariable("password") String password) {
		ClientFacade facade = loginManager.login(email, password, ClientType.Adminstrator);

		boolean isLogged = facade != null;

		LoginResult result = new LoginResult();
		result.setLogged(isLogged);
		if (isLogged) {
			result.setToken(tokensManager.generateToken(facade, ClientType.Adminstrator));
		}

		return ResponseEntity.ok().body(result);
	}

	@PostMapping("api/admin/company")
	public ResponseEntity<Void> addCompany(@RequestBody Company company, @RequestHeader("token") String token)
			throws ValidationException {
		try {
			ClientFacade facade = tokensManager.getFacadeByToken(token);

			if (facade == null || !(facade instanceof AdminFacade)) {
				return new ResponseEntity<Void>(HttpStatus.UNAUTHORIZED);
			}

			AdminFacade adminFacade = (AdminFacade) facade;

			adminFacade.addCompany(company);
		} catch (ValidationException ex) {
			throw ex;
		} catch (Exception ex) {
			System.out.println("Failed to add company " + ex.getMessage());
			return new ResponseEntity<Void>(HttpStatus.INTERNAL_SERVER_ERROR);
		}

		return new ResponseEntity<Void>(HttpStatus.OK);
	}

	@PutMapping("api/admin/company")
	public ResponseEntity<Void> updateCompany(@RequestBody Company company, @RequestHeader("token") String token) {
		try {
			ClientFacade facade = tokensManager.getFacadeByToken(token);

			if (facade == null || !(facade instanceof AdminFacade)) {
				return new ResponseEntity<Void>(HttpStatus.UNAUTHORIZED);
			}

			AdminFacade adminFacade = (AdminFacade) facade;

			adminFacade.updateCompany(company);
		} catch (Exception ex) {
			return new ResponseEntity<Void>(HttpStatus.INTERNAL_SERVER_ERROR);
		}

		return new ResponseEntity<Void>(HttpStatus.OK);
	}

	@DeleteMapping("api/admin/company/{companyId}")
	public ResponseEntity<Void> deleteCompany(@PathVariable("companyId") Integer companyId,
			@RequestHeader("token") String token) {
		try {
			ClientFacade facade = tokensManager.getFacadeByToken(token);

			if (facade == null || !(facade instanceof AdminFacade)) {
				return new ResponseEntity<Void>(HttpStatus.UNAUTHORIZED);
			}

			AdminFacade adminFacade = (AdminFacade) facade;

			adminFacade.deleteCompany(companyId);
		} catch (ValidationException ex) {
			return new ResponseEntity<Void>(HttpStatus.BAD_REQUEST);
		} catch (Exception ex) {
			return new ResponseEntity<Void>(HttpStatus.INTERNAL_SERVER_ERROR);
		}

		return new ResponseEntity<Void>(HttpStatus.OK);
	}

	@GetMapping("api/admin/company")
	public ResponseEntity<List<Company>> getAllCompanies(@RequestHeader("token") String token) {
		List<Company> compancies = null;

		try {
			ClientFacade facade = tokensManager.getFacadeByToken(token);

			if (facade == null || !(facade instanceof AdminFacade)) {
				return new ResponseEntity<List<Company>>(HttpStatus.UNAUTHORIZED);
			}

			AdminFacade adminFacade = (AdminFacade) facade;

			compancies = adminFacade.getAllCompanies();
		} catch (Exception ex) {
			return new ResponseEntity<List<Company>>(HttpStatus.INTERNAL_SERVER_ERROR);
		}

		return new ResponseEntity<List<Company>>(compancies, HttpStatus.OK);
	}

	@GetMapping("api/admin/company/{companyId}")
	public ResponseEntity<Company> getOneCompany(@PathVariable("companyId") int companyId,
			@RequestHeader("token") String token) {
		Company company = null;

		try {
			ClientFacade facade = tokensManager.getFacadeByToken(token);

			if (facade == null || !(facade instanceof AdminFacade)) {
				return new ResponseEntity<Company>(HttpStatus.UNAUTHORIZED);
			}

			AdminFacade adminFacade = (AdminFacade) facade;

			company = adminFacade.getOneCompany(companyId);
		} catch (Exception ex) {
			return new ResponseEntity<Company>(HttpStatus.INTERNAL_SERVER_ERROR);
		}

		return new ResponseEntity<Company>(company, HttpStatus.OK);
	}

	@PostMapping("api/admin/customer")
	public ResponseEntity<Void> addCustomer(@RequestBody Customer customer, @RequestHeader("token") String token) {
		try {
			ClientFacade facade = tokensManager.getFacadeByToken(token);

			if (facade == null || !(facade instanceof AdminFacade)) {
				return new ResponseEntity<Void>(HttpStatus.UNAUTHORIZED);
			}

			AdminFacade adminFacade = (AdminFacade) facade;

			adminFacade.addCustomer(customer);
		} catch (ValidationException ex) {
			return new ResponseEntity<Void>(HttpStatus.BAD_REQUEST);
		} catch (Exception ex) {
			return new ResponseEntity<Void>(HttpStatus.INTERNAL_SERVER_ERROR);
		}

		return new ResponseEntity<Void>(HttpStatus.OK);
	}

	@PutMapping("api/admin/customer")
	public ResponseEntity<Void> updateCustomer(@RequestBody Customer customer, @RequestHeader("token") String token) {
		try {
			ClientFacade facade = tokensManager.getFacadeByToken(token);

			if (facade == null || !(facade instanceof AdminFacade)) {
				return new ResponseEntity<Void>(HttpStatus.UNAUTHORIZED);
			}

			AdminFacade adminFacade = (AdminFacade) facade;

			adminFacade.updateCustomer(customer);
		} catch (Exception ex) {
			return new ResponseEntity<Void>(HttpStatus.INTERNAL_SERVER_ERROR);
		}

		return new ResponseEntity<Void>(HttpStatus.OK);
	}

	@DeleteMapping("api/admin/customer/{customerId}")
	public ResponseEntity<Void> deleteCustomer(@PathVariable("customerId") int customerId,
			@RequestHeader("token") String token) {
		try {
			ClientFacade facade = tokensManager.getFacadeByToken(token);

			if (facade == null || !(facade instanceof AdminFacade)) {
				return new ResponseEntity<Void>(HttpStatus.UNAUTHORIZED);
			}

			AdminFacade adminFacade = (AdminFacade) facade;

			adminFacade.deleteCustomer(customerId);
		} catch (Exception ex) {
			return new ResponseEntity<Void>(HttpStatus.INTERNAL_SERVER_ERROR);
		}

		return new ResponseEntity<Void>(HttpStatus.OK);
	}

	@GetMapping("api/admin/customer")
	public ResponseEntity<List<Customer>> getAllCustomers(@RequestHeader("token") String token) {
		List<Customer> customers = null;

		try {
			ClientFacade facade = tokensManager.getFacadeByToken(token);

			if (facade == null || !(facade instanceof AdminFacade)) {
				return new ResponseEntity<List<Customer>>(HttpStatus.UNAUTHORIZED);
			}

			AdminFacade adminFacade = (AdminFacade) facade;

			customers = adminFacade.getAllCustomers();
		} catch (Exception ex) {
			return new ResponseEntity<List<Customer>>(HttpStatus.INTERNAL_SERVER_ERROR);
		}

		return new ResponseEntity<List<Customer>>(customers, HttpStatus.OK);
	}

	@GetMapping("api/admin/customer/{customerId}")
	public ResponseEntity<Customer> getOneCustomer(@PathVariable("customerId") Integer customerId,
			@RequestHeader("token") String token) {
		Customer customer = null;

		try {
			ClientFacade facade = tokensManager.getFacadeByToken(token);

			if (facade == null || !(facade instanceof AdminFacade)) {
				return new ResponseEntity<Customer>(HttpStatus.UNAUTHORIZED);
			}

			AdminFacade adminFacade = (AdminFacade) facade;

			customer = adminFacade.getOneCustomer(customerId);
		} catch (Exception ex) {
			return new ResponseEntity<Customer>(HttpStatus.INTERNAL_SERVER_ERROR);
		}

		return new ResponseEntity<Customer>(customer, HttpStatus.OK);
	}

	@Override
	@GetMapping("api/admin/logout")
	public ResponseEntity<Void> logout(@RequestHeader("token") String token) {
		boolean isDeleted = tokensManager.deleteToken(token, ClientType.Adminstrator);

		if (isDeleted) {
			return new ResponseEntity<Void>(HttpStatus.OK);
		} else {
			return new ResponseEntity<Void>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

}
