package com.acme;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

import com.acme.model.AccountDetail;
import com.acme.repository.AccountDetailRepository;

import springfox.documentation.swagger2.annotations.EnableSwagger2;

@EnableWebMvc
@SpringBootApplication
@EnableSwagger2
public class AccountManagerApplication {
	
	@Autowired
	AccountDetailRepository accountDetailRepository;

	public static void main(String[] args) {
		SpringApplication.run(AccountManagerApplication.class, args);
		
	}
	
	@Bean
	InitializingBean sendDatabase() {
	    return () -> {
	    	if(accountDetailRepository.findAll().size() == 0)
	    	{
		    	accountDetailRepository.saveAll(createInitialRecords());
	    	}
	      };

		}
	
	public List<AccountDetail> createInitialRecords()
	{
		List<AccountDetail> accountDetailList = new ArrayList<AccountDetail>();
    	AccountDetail accountDetail = new AccountDetail();
    	accountDetail.setAccountName("Dexter");
    	accountDetail.setAccountNumber(12345678);
    	accountDetail.setBankName("acme");
    	accountDetail.setCurrenyCode("HKD");
    	accountDetail.setCurrentBalance(new BigDecimal(100000.00));
    	AccountDetail accountDetail1 = new AccountDetail();
    	accountDetail1.setAccountName("Anand");
    	accountDetail1.setAccountNumber(88888888);
    	accountDetail1.setBankName("acme");
    	accountDetail1.setCurrenyCode("HKD");
    	accountDetail1.setCurrentBalance(new BigDecimal(100000.00));
    	accountDetailList.add(accountDetail);
    	accountDetailList.add(accountDetail1);
    	return accountDetailList;
    	

	}
	
	
}
