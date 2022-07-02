package com.acme.service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.acme.contants.AccountConstants;
import com.acme.dto.Account;
import com.acme.dto.Transfer;
import com.acme.exception.AccountException;
import com.acme.model.AccountDetail;
import com.acme.repository.AccountDetailRepository;
import com.acme.result.Result;
import com.acme.validate.AccountValidator;

@Service
public class AccountService {
	
	@Autowired	
	AccountDetailRepository repository;
	
	@SuppressWarnings("rawtypes")
	public Result fundTransfer(Transfer transfer, List<Integer> accountNumList, Account account) throws AccountException {
	try
	{
		List<AccountDetail> entry = repository.findAllByAccountNumberIn(accountNumList); 
		if(entry != null && entry.size() == 2)
		{
			for(AccountDetail entity : entry)
			{
				if(entity.getAccountNumber().compareTo(transfer.getFromAccountNumber()) == 0)
				{
					if(entity.getCurrentBalance().doubleValue() > transfer.getAmountToTransfer())
					{
						entity.setCurrentBalance(entity.getCurrentBalance().subtract(new BigDecimal(transfer.getAmountToTransfer())));
					}
					else
					{
						return new Result(AccountConstants.INSUFFICIENT_FUNDS);
					}
				}
				else if(entity.getAccountNumber().compareTo(transfer.getToAccountNumber()) == 0)
				{
					entity.setCurrentBalance(entity.getCurrentBalance().add(new BigDecimal(transfer.getAmountToTransfer())));
				}
			}	
			repository.saveAll(entry);
			account.setAccountNumber(transfer.getFromAccountNumber());
			account.setAccountHolder(transfer.getAccountHolder());
			Result result = getAccountBalance(account);
			return result;
		}
		else{
			return new Result(AccountConstants.NO_MATCHING_ACCOUNT_FOUND);
		}
	}
	catch(Exception e)
	{
		throw new AccountException(e.getLocalizedMessage());
	}

}
	@SuppressWarnings("rawtypes")
	public Result getAccountBalance(Account account) throws AccountException {
		try{
			List<AccountDetail> accountDetailList = new ArrayList<AccountDetail>();
			Map<String, Object> result = new HashMap<>();
			accountDetailList = repository.findByAccountNumber(account.getAccountNumber());
			if(accountDetailList.size() == 0)
			{
				return new Result(AccountConstants.NO_MATCHING_ACCOUNT_FOUND);
			}
			else
			{
				result.put("data", accountDetailList);
				return new Result(result.get("data"));
			}
		}catch(Exception e)
		{
			throw new AccountException(e.getLocalizedMessage());
		}
		
	}
	
	public Result transfer(Transfer transfer) throws AccountException {
		List<Integer> accountNumList = new ArrayList<>();
		accountNumList.add(transfer.getFromAccountNumber());
		accountNumList.add(transfer.getToAccountNumber());
		Account account = new Account();
		
		AccountValidator validator = new AccountValidator();
		List<String> violations = validator.validateAmount(transfer.getAmountToTransfer());
		if(violations != null && violations.size() == 0)
		{
			return fundTransfer(transfer, accountNumList, account);
		}
		else
		{
			return new Result(violations);
		}
	}
	
	
	public List<String> validateAccountDetails(Account account) {
		AccountValidator validator = new AccountValidator();
		List<String> violations = validator.validateAccountDetail(account);
		return violations;
	}

}