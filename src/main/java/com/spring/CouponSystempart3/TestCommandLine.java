package com.spring.CouponSystempart3;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import com.spring.CouponSystempart3.beans.Category;
import com.spring.CouponSystempart3.beans.ClientType;
import com.spring.CouponSystempart3.beans.Company;
import com.spring.CouponSystempart3.beans.Coupon;
import com.spring.CouponSystempart3.beans.Customer;
import com.spring.CouponSystempart3.exceptions.CouponAlredyExsitsException;
import com.spring.CouponSystempart3.exceptions.CouponExpiredException;
import com.spring.CouponSystempart3.exceptions.CouponOutOfStockException;
import com.spring.CouponSystempart3.exceptions.ValidationException;
import com.spring.CouponSystempart3.facade.AdminFacade;
import com.spring.CouponSystempart3.facade.CompanyFacade;
import com.spring.CouponSystempart3.facade.CustomerFacade;
import com.spring.CouponSystempart3.job.CouponExpirationDailyJob;

@Component
@Order(1)
public class TestCommandLine implements CommandLineRunner {
	@Autowired
	private LoginManager loginManager; 
	@Autowired
	private CouponExpirationDailyJob job;
	
	@Override
	public void run(String... args) throws Exception {
		AdminFacade adminFacade = (AdminFacade)loginManager.login("admin@admin.com", "admin", ClientType.Adminstrator);
		
		startJob();
		testLogin(adminFacade);
		testAdmin(adminFacade);
		testCompany();
		testCustomer();
		stopJob();
	}
	
	private void startJob() {
		Thread thread = new Thread(job);
		thread.start();
	}

	private void stopJob() {
		job.stop();
	}
	
	private void testLogin(AdminFacade adminFacade) {
		System.out.println("Starting test login");
		boolean loggedIn = adminFacade.login("admin@admin.com", "admin");
		
		System.out.println("admin@admin.com login status = " + loggedIn);
		
		loggedIn = adminFacade.login("blabla", "blabla");
		
		System.out.println("blabla login status = " + loggedIn);
		
		System.out.println("Finished test login");
	}

	private void testAdmin(AdminFacade adminFacade) throws ValidationException {
		Company c1 = new Company();
		c1.setEmail("d1");
		c1.setName("daniel");
		c1.setPassword("1234");
		
		Company c2 = new Company();
		c2.setEmail("d2");
		c2.setName("david");
		c2.setPassword("123");

		adminFacade.addCompany(c1);
		adminFacade.addCompany(c2);
		System.out.println("\nAdd company");
		List<Company> companies = adminFacade.getAllCompanies();
		printCompanies(companies);
		c1 = adminFacade.getOneCompany(1);
		System.out.println("\nGet one company");
		System.out.println("\nCompany id: " + c1.getId() + "\nName : " + c1.getName() + "\nEmail: " + c1.getEmail()
				+ "\nPassword: " + c1.getPassword());
		System.out.println("\nDelete company 1");
		adminFacade.deleteCompany(1);
		companies = adminFacade.getAllCompanies();
		printCompanies(companies);
		c2.setName("shlomo");
		c2.setId(2);
		c2.setEmail("@@@@@@@@");
		c2.setPassword("1234567890");
		adminFacade.updateCompany(c2);

		companies = adminFacade.getAllCompanies();
		System.out.println("\nUpdate company");
		printCompanies(companies);
		System.out.println("\nADD Customers");
		Customer a = new Customer();
		a.setFirstName("David");
		a.setLastName("ABCD");
		a.setEmail("@@@");
		a.setPassword("11111");
		Customer b = new Customer();
		b.setFirstName("Shimon");
		b.setLastName("DDDD");
		b.setEmail("@@@YYY");
		b.setPassword("22222");
		adminFacade.addCustomer(a);
		adminFacade.addCustomer(b);
		List<Customer> customers = adminFacade.getAllCustomers();
		printCustomers(customers);
		System.out.println("\nUpdate customer");
		a.setId(1);
		a.setFirstName("Michael");
		a.setLastName("TTTTTTTT");
		a.setEmail("@@@");
		a.setPassword("123");
		adminFacade.updateCustomer(a);
		customers = adminFacade.getAllCustomers();
		printCustomers(customers);
		System.out.println("\nget one costumer");
		a = adminFacade.getOneCustomer(2);
		customers = new ArrayList<>();
		customers.add(a);
		printCustomers(customers);
		System.out.println("\nDelete customer 2");
		adminFacade.deleteCustomer(2);
		printCustomers(adminFacade.getAllCustomers());
	}

	private void testCompany() throws ValidationException {
		CompanyFacade companyFacade = (CompanyFacade)loginManager.login("@@@@@@@@", "1234567890",
				ClientType.Company);
		Coupon a = new Coupon();
		a.setCategory(Category.Food);
		a.setTitle("BOB");
		a.setDescription("LOL");
		a.setStartDate(new Date("20/2/2020"));
		a.setEndDate(new Date("20/10/2020"));
		a.setAmount(15);
		a.setPrice(150.0);
		a.setImage("AAAAAAAAAAAAAAAAAAAA");
		Coupon b = new Coupon();
		b.setCategory(Category.Food);
		b.setTitle("RESTAURANT");
		b.setDescription("RRRRRRRRRRRRR");
		b.setStartDate(new Date("20/2/2020"));
		b.setEndDate(new Date("20/10/2020"));
		b.setAmount(15);
		b.setPrice(200.0);
		b.setImage("BBBBBBBBBBBBBBBBB");
		companyFacade.addCoupon(a);
		companyFacade.addCoupon(b);
		System.out.println("\nADD coupons");
		printCoupons(companyFacade.getCompanyCoupons());
		System.out.println("\nUpdate coupon 1");
		a.setId(1);
		a.setTitle("CCCCCCCCC");
		a.setDescription("CCCCCCCCCC");
		a.setStartDate(new Date("20/2/2020"));
		a.setEndDate(new Date("13/10/2020"));
		a.setAmount(3333);
		a.setPrice(333);
		a.setImage("CCCCCCCCCCCCCCCCCC");
		companyFacade.updateCoupon(a);
		printCoupons(companyFacade.getCompanyCoupons());
		System.out.println("\nDelete coupon 2");
		companyFacade.deleteCoupon(2);
		printCoupons(companyFacade.getCompanyCoupons());
		System.out.println("\nget coupons by category 1");
		printCoupons(companyFacade.getCompanyCoupons(Category.Food));
		System.out.println("\nget coupons under max price 100");
		printCoupons(companyFacade.getCompanyCoupons(100));
		System.out.println("\nget company details");
		ArrayList<Company> temp = new ArrayList<>();
		temp.add(companyFacade.getCompanyDetails());
		printCompanies(temp);
	}

	private void testCustomer()
			throws CouponOutOfStockException, CouponExpiredException, CouponAlredyExsitsException {
		CustomerFacade customerFacade = (CustomerFacade) loginManager.login("@@@", "123",
				ClientType.Customer);
		Coupon a = new Coupon();
		a.setId(1);
		a.setCategory(Category.Food);
		a.setCompanyId(2);
		a.setTitle("BOB");
		a.setDescription("LOL");
		a.setStartDate(new Date("20/2/2020"));
		a.setEndDate(new Date("20/10/2020"));
		a.setAmount(15);
		a.setPrice(150.0);
		a.setImage("AAAAAAAAAAAAAAAAAAAA");
		
		System.out.println("\nPurchase coupon ");
		customerFacade.purchaseCoupon(a);
		System.out.println(a);
		
		System.out.println("\nget customers coupons");
		printCoupons(customerFacade.getCustomerCoupons());
		System.out.println("\nGet customers coupons by category");
		printCoupons(customerFacade.getCustomerCoupons(Category.Food));
		System.out.println("\nGet customers coupons by max price");
		printCoupons(customerFacade.getCustomerCoupons(200));
		System.out.println("\nprint customer details");
		ArrayList<Customer> customer = new ArrayList<>();
		customer.add(customerFacade.getCustomerDetails());
		printCustomers(customer);
	}

	
	private void printCompanies(List<Company> list) {
		for (Company i : list) {
			System.out.println("\nCompany id: " + i.getId() + "\nName : " + i.getName() + "\nEmail: " + i.getEmail()
					+ "\nPassword: " + i.getPassword());
			printCoupons( i.getCoupons() );
		}
	}

	private void printCustomers(List<Customer> list) {
		for (Customer i : list) {
			System.out.println("\nCustomer id: " + i.getId() + "\nFirst Name: " + i.getFirstName() + "\nLast name: "
					+ i.getLastName() + "\nEmail: " + i.getEmail() + "\nPassword: " + i.getPassword());
			printCoupons( i.getCoupons() );
		}
	}

	private void printCoupons(List<Coupon> coupons) {
		if(coupons==null)
			return;
		for (Coupon i : coupons)
			System.out.println("\nCoupon id: " + i.getId() + "\nCompany id: " + i.getCompanyId() + "\nCategory id: "
					+ i.getCategory() + "\nTitle: " + i.getTitle() + "\nDescription: " + i.getDescription()
					+ "\nStart Date: " + i.getStartDate() + "\nEnd Date: " + i.getEndDate() + "\nAmount: "
					+ i.getAmount() + "\nPrice: " + i.getPrice() + "\nImage: " + i.getImage());
	}
}
