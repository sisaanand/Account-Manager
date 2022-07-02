package com.acme.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.acme.model.AccountDetail;

public interface AccountDetailRepository extends JpaRepository<AccountDetail, Long> {
	
	List<AccountDetail> findByAccountNumber(Integer account_number );
	
	List<AccountDetail> findAllByAccountNumberIn(Iterable<Integer> accountNumber );

	

}
