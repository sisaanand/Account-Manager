package com.acme;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.platform.runner.JUnitPlatform;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import com.acme.dto.Account;
import com.acme.dto.Transfer;
import com.acme.model.AccountDetail;
import com.acme.repository.AccountDetailRepository;
import com.acme.result.Result;
import com.fasterxml.jackson.databind.ObjectMapper;

@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@AutoConfigureMockMvc
public class AccountManagerApplicationTest {
	
	ObjectMapper om = new ObjectMapper();
	
	@Autowired
	MockMvc mockMvc;
	 
	@Autowired
	AccountDetailRepository repository;
	 
    @BeforeEach
    public void setup() {
    	insertTestData();
	}

	@Test
    public void testGetAccountBalance() throws Exception {
       	Account expectedRecord = getTestData().get(0);     
       	
        mockMvc.perform(post("/bank/getAccountBalance")
                .contentType("application/json")
                .content(om.writeValueAsString(expectedRecord)))
                .andDo(print())
                .andExpect(jsonPath("$.completed").value("true"))
                .andExpect(jsonPath("$.data[0].currentBalance").value("9000.0"));
    }
	
	@Test
    public void testGetAccountExists() throws Exception {
		Account expectedRecord = getTestData().get(0);
		expectedRecord.setAccountNumber(12982310);
    	Result actualRecord = om.readValue(mockMvc.perform(post("/bank/getAccountBalance")
                .contentType("application/json")
                .content(om.writeValueAsString(expectedRecord)))
                .andDo(print())
                .andExpect(status().isNotFound()).andReturn().getResponse().getContentAsString(), Result.class); 

    	assertEquals(actualRecord.isCompleted(), false);
    	assertEquals(actualRecord.getErrorMsg(), "No matching account found");
    }
	
	@Test
    public void testwithNegativeAccountNumber() throws Exception {
		Account expectedRecord = getTestData().get(0);
		expectedRecord.setAccountNumber(-123456);
    	Result actualRecord = om.readValue(mockMvc.perform(post("/bank/getAccountBalance")
                .contentType("application/json")
                .content(om.writeValueAsString(expectedRecord)))
                .andDo(print())
                .andExpect(status().isBadRequest()).andReturn().getResponse().getContentAsString(), Result.class); 

    	assertEquals(actualRecord.isCompleted(), false);
    	assertEquals(actualRecord.getErrorMsg(), "Validation Error");
    	for(int i=0; i<actualRecord.getViolations().size(); i++)
    		assertEquals(actualRecord.getViolations().get(i), "Account Number is not in range");
    }
	
	@Test
    public void testwithinvalidAccountHolderName() throws Exception {
		Account expectedRecord = getTestData().get(0);
		expectedRecord.setAccountHolder("Cap");
    	Result actualRecord = om.readValue(mockMvc.perform(post("/bank/getAccountBalance")
                .contentType("application/json")
                .content(om.writeValueAsString(expectedRecord)))
                .andDo(print())
                .andExpect(status().isBadRequest()).andReturn().getResponse().getContentAsString(), Result.class); 

    	assertEquals(actualRecord.isCompleted(), false);
    	assertEquals(actualRecord.getErrorMsg(), "Validation Error");
    	assertTrue(actualRecord.getViolations().contains("Account Holder should not contain special characters and length between 5 to 29 characters"));
	}
	
	@Test
    public void testValidAccountTransfer() throws Exception {
		Account fromAccount = getTestData().get(0);
		Account toAccount = getTestData().get(1);
		
		Transfer accountTransfer = new Transfer();
		accountTransfer.setFromAccountNumber(fromAccount.getAccountNumber());
		accountTransfer.setToAccountNumber(toAccount.getAccountNumber());
		accountTransfer.setAmountToTransfer(500.00);
		accountTransfer.setAccountHolder(fromAccount.getAccountHolder());
		
    	mockMvc.perform(post("/bank/transfer")
                .contentType("application/json")
                .content(om.writeValueAsString(accountTransfer)))
                .andDo(print())
                .andExpect(jsonPath("$.completed").value("true"))
                .andExpect(jsonPath("$.data[0].currentBalance").value("8500.0"));
	}
	
	@Test
    public void testAccountTransferwithNegativeAmount() throws Exception {
		Account fromAccount = getTestData().get(0);
		Account toAccount = getTestData().get(1);
		
		Transfer accountTransfer = new Transfer();
		accountTransfer.setFromAccountNumber(fromAccount.getAccountNumber());
		accountTransfer.setToAccountNumber(toAccount.getAccountNumber());
		accountTransfer.setAmountToTransfer(-500.00);
		accountTransfer.setAccountHolder(fromAccount.getAccountHolder());
		
		Result actualRecord = om.readValue(mockMvc.perform(post("/bank/transfer")
                .contentType("application/json")
                .content(om.writeValueAsString(accountTransfer)))
                .andDo(print())
                .andExpect(status().isBadRequest()).andReturn().getResponse().getContentAsString(), Result.class); 

    	assertEquals(actualRecord.isCompleted(), false);
    	assertEquals(actualRecord.getErrorMsg(), "Validation Error");
    	for(int i=0; i<actualRecord.getViolations().size(); i++)
    		assertEquals(actualRecord.getViolations().get(i), "amount should be greater than zero");
	}
	
	@Test
    public void testAccountTransferwithInsufficientBalance() throws Exception {
		Account fromAccount = getTestData().get(0);
		Account toAccount = getTestData().get(1);
		
		Transfer accountTransfer = new Transfer();
		accountTransfer.setFromAccountNumber(fromAccount.getAccountNumber());
		accountTransfer.setToAccountNumber(toAccount.getAccountNumber());
		accountTransfer.setAmountToTransfer(10500.00);
		accountTransfer.setAccountHolder(fromAccount.getAccountHolder());
		
		Result actualRecord = om.readValue(mockMvc.perform(post("/bank/transfer")
                .contentType("application/json")
                .content(om.writeValueAsString(accountTransfer)))
                .andDo(print())
                .andExpect(status().isBadRequest()).andReturn().getResponse().getContentAsString(), Result.class); 

    	assertEquals(actualRecord.isCompleted(), false);
    	assertEquals(actualRecord.getErrorMsg(), "Insufficient funds");
                
	}
	
    
    private List<Account> getTestData() {
        List<Account> data = new ArrayList<>();
        
        Account account = new Account();
        account.setAccountNumber(123456);
        account.setAccountHolder("CaptainAmerica");
        account.setCurrentBalance(9000.00);
        account.setBankName("acme");
        data.add(account);

        Account account1 = new Account();
        account1.setAccountNumber(555555);
        account1.setAccountHolder("IronMan");
        account1.setCurrentBalance(9000.00);
        account1.setBankName("acme");
        data.add(account1);
        
        Account account2 = new Account();
        account2.setAccountNumber(6666666);
        account2.setAccountHolder("SpiderMan");
        account2.setCurrentBalance(5000.00);
        account2.setBankName("acme");
        data.add(account2);
        
        return data;
    }
    
    private void insertTestData()
    {
    	List<Account> accountList = getTestData();
    	List<AccountDetail> accountDetailList = new ArrayList<>();
    	for(Account account : accountList)
    	{
    		AccountDetail detail = new AccountDetail();
    		detail.setAccountName(account.getAccountHolder());
    		detail.setAccountNumber(account.getAccountNumber());
    		detail.setCurrentBalance(BigDecimal.valueOf(account.getCurrentBalance()));
    		detail.setBankName(account.getBankName());
    		detail.setCurrenyCode("HKD");   
    		accountDetailList.add(detail);    		
    	}
    	repository.saveAll(accountDetailList);
    }
    
    
    
}
