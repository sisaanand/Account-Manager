package com.acme.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public class Transfer {
	
	@JsonProperty("fromAccountNumber" )
	private int fromAccountNumber;
	
	@JsonProperty("toAccountNumber" )
	private int toAccountNumber;
	
	@JsonProperty("accountHolder")
	private String accountHolder;
	
	@JsonProperty("amountToTransfer")
	private double amountToTransfer;
	
	@JsonProperty("bankName")
	private String bankName;

	public int getFromAccountNumber() {
		return fromAccountNumber;
	}

	public void setFromAccountNumber(int fromAccountNumber) {
		this.fromAccountNumber = fromAccountNumber;
	}

	public int getToAccountNumber() {
		return toAccountNumber;
	}

	public void setToAccountNumber(int toAccountNumber) {
		this.toAccountNumber = toAccountNumber;
	}

	public String getAccountHolder() {
		return accountHolder;
	}

	public void setAccountHolder(String accountHolder) {
		this.accountHolder = accountHolder;
	}

	public double getAmountToTransfer() {
		return amountToTransfer;
	}

	public void setAmountToTransfer(double amountToTransfer) {
		this.amountToTransfer = amountToTransfer;
	}

	public String getBankName() {
		return bankName;
	}

	public void setBankName(String bankName) {
		this.bankName = bankName;
	}
	
	
	

}
