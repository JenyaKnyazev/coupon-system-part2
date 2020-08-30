package com.spring.CouponSystempart2;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

import com.spring.CouponSystempart2.LoginManager;
import com.spring.CouponSystempart2.facade.AdminFacade;
import com.spring.CouponSystempart2.facade.ClientFacade;
import com.spring.CouponSystempart2.facade.CompanyFacade;
import com.spring.CouponSystempart2.facade.CustomerFacade;
import com.spring.CouponSystempart2.beans.ClientType;

@Component
public class LoginManager {
	@Autowired
	private ApplicationContext ctx;
	
	public ClientFacade login(String email, String password, ClientType clientType) {
		ClientFacade facade = null;

		switch (clientType) {
		case Adminstrator:
			facade = ctx.getBean(AdminFacade.class);
			break;
		case Company:
			facade = ctx.getBean(CompanyFacade.class);
			break;
		case Customer:
			facade = ctx.getBean(CustomerFacade.class);
			break;
		}

		if (facade == null) {
			return null;
		}

		return facade.login(email, password) ? facade : null;
	}
}