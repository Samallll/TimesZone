package com.timeszone.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.timeszone.model.Otp;
 
@Repository
public interface OtpRepository extends JpaRepository<Otp,Integer>{
	
	Otp findByEmailId(String emailId);
}
