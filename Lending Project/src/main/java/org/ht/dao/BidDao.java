package org.ht.dao;

import java.util.List;
import java.util.Map;

import org.ht.pojo.Borrowcord;
import org.ht.pojo.Borrowmoney;
import org.ht.pojo.Certification;
import org.ht.pojo.InvestInfo;
import org.ht.pojo.Product;
import org.ht.pojo.Trade;
import org.ht.pojo.Users;
import org.ht.pojo.Withdrawal;

public interface BidDao {

	public List<Product> todaoqi();

	public void upzt(Integer id);

	public List<InvestInfo> totouzilist(Integer id);
	
	public Users seluesr(Integer uid);
	

	// ------------Project due----------------//
	
	public Certification togetyue(Integer uID);

	public void toupyue(Map<String, String> map);

	public void toaddtrade(Trade trade);
	
	// ------------Fundraising due----------------//
	
	public List<Product> togetck();
	
	public void upzts(Integer id);
	
	// ------------Home page display information----------------//
	
	public List<Borrowmoney> tosize();
	public List<Withdrawal> tosizew();
	public List<Borrowcord> tosizeb();

}