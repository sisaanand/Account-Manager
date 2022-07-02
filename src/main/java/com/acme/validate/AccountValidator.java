package com.acme.validate;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.acme.dto.Account;

public class AccountValidator {
	
	List<String> violations = new ArrayList<>();
	
	public List<String> validateAccountDetail(Account account)
	{
		
		if(account != null && account.getAccountHolder() == null)
			violations.add("Account Holder should not be empty");
		else validateWithRegex(account.getAccountHolder(), "^[A-Za-z]\\w{5,29}$", 
				"Account Holder should not contain special characters and length between 5 to 29 characters");
		
		if(account != null && (account.getAccountNumber() > 999999999 || account.getAccountNumber() < 0 ))
			violations.add("Account Number is not in range");
		
		return violations;
	}
	
	
	 public void validateWithRegex(String input, String regex, String errorMsg )
	 {
        Pattern p = Pattern.compile(regex);
        Matcher m = p.matcher(input);
        if(!m.matches())
        	violations.add(errorMsg);
	 }
	 
	 public List<String> validateAccountTransfer(double amount, double currentBalance)
	 {
		 
		 if(amount < 0.00 || currentBalance < 0.00)
			 violations.add("amount should be greater than zero");
		 
		 if(currentBalance < amount)
			 violations.add("Insufficient funds");
			 
		 return violations;
	 }
	 
	 public List<String> validateAmount(double amount)
	 {		 
		 if(amount < 0.00 )
			 violations.add("amount should be greater than zero");
			 
		 return violations;
	 }
	  
	 
	 
	 

}
