package com.acme.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

public class Account {
	
	@JsonProperty("accountNumber" )
	private int accountNumber;
	
	@JsonProperty("accountHolder")
	private String accountHolder;
	
	@JsonProperty("currentBalance")
	@JsonIgnoreProperties(ignoreUnknown=true)
	private double currentBalance;
	
	@JsonIgnoreProperties(ignoreUnknown=true)
	@JsonProperty("bankName")
	private String bankName;
	
	public int getAccountNumber() {
		return accountNumber;
	}
	public void setAccountNumber(int accountNumber) {
		this.accountNumber = accountNumber;
	}
	public String getAccountHolder() {
		return accountHolder;
	}
	public void setAccountHolder(String accountHolder) {
		this.accountHolder = accountHolder;
	}
	public double getCurrentBalance() {
		return currentBalance;
	}
	public void setCurrentBalance(double currentBalance) {
		this.currentBalance = currentBalance;
	}
	public String getBankName() {
		return bankName;
	}
	public void setBankName(String bankName) {
		this.bankName = bankName;
	}
	

}
