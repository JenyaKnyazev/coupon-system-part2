package com.spring.CouponSystempart3.beans;

import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.transaction.Transactional;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Company {
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "COMPANY_SEQ")
	private int id;
	private String name;
	private String email;
	private String password;
	@OneToMany(fetch = FetchType.EAGER, cascade = {CascadeType.PERSIST, CascadeType.MERGE,CascadeType.REFRESH})
	@JoinColumn(name = "companyId")
	private List<Coupon> coupons;
	
	
	public void removeChildren(Coupon coupon){
	    this.getCoupons().remove(coupon);
	    coupon.setCompanyId(null);
	}
}
