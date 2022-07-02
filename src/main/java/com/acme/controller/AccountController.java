package com.acme.controller;

import java.util.ArrayList;
import java.util.List;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.acme.contants.AccountConstants;
import com.acme.dto.Account;
import com.acme.dto.Transfer;
import com.acme.exception.AccountException;
import com.acme.result.Result;
import com.acme.service.AccountService;
import com.acme.validate.AccountValidator;

@RestController
@RequestMapping("/bank")
public class AccountController {
	
@Autowired
AccountService service;
	
@SuppressWarnings("rawtypes")
@RequestMapping(
		value = "/getAccountBalance",
		consumes=MediaType.APPLICATION_JSON_VALUE,
		produces=MediaType.APPLICATION_JSON_VALUE,
		method = RequestMethod.POST)
public ResponseEntity getAccountBalance(@RequestBody Account account) throws AccountException
{
	if(account == null)
		return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new Result(AccountConstants.REQUEST_BODY_IS_EXPECTED));
	if(service.validateAccountDetails(account).size() > 0)
		return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new Result(service.validateAccountDetails(account)));
	Result result = service.getAccountBalance(account);
	if(result.getData() != null)
		return ResponseEntity.status(HttpStatus.OK).body(result);
	else
		return ResponseEntity.status(HttpStatus.NOT_FOUND).body(result);
}


@SuppressWarnings("rawtypes")
@RequestMapping(
		value = "/transfer",
		consumes=MediaType.APPLICATION_JSON_VALUE,
		produces=MediaType.APPLICATION_JSON_VALUE,
		method = RequestMethod.POST)
public ResponseEntity transfer(@RequestBody Transfer transfer) throws AccountException
{
	if(transfer == null) {
		return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new Result(AccountConstants.REQUEST_BODY_IS_EXPECTED));
	}
	List<Integer> accountNumList = new ArrayList<>();
	accountNumList.add(transfer.getFromAccountNumber());
	accountNumList.add(transfer.getToAccountNumber());
	Account account = new Account();
	AccountValidator validator = new AccountValidator();
	List<String> violations = validator.validateAmount(transfer.getAmountToTransfer());
	if(violations != null && violations.size() == 0)
	{
		Result result = service.fundTransfer(transfer, accountNumList, account);
		if(result.getData() != null)
			return ResponseEntity.status(HttpStatus.OK).body(result);
		else
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(result);
	}
	else
		return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new Result(violations));
}

@SuppressWarnings("rawtypes")
@ExceptionHandler(AccountException.class)
public ResponseEntity handleAccountException(AccountException ex) {
	return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new Result(ex.getLocalizedMessage().toString()));
}

}
