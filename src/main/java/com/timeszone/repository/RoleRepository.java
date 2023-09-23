package com.timeszone.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
//import org.springframework.data.jpa.repository.Modifying;
//import org.springframework.data.jpa.repository.Query;

import com.timeszone.model.customer.Role;


public interface RoleRepository extends JpaRepository<Role,Integer>{
	
	Optional<Role> findByAuthority(String authority);
	
//	@Modifying
//    @Query("UPDATE Role r SET r.authority = :newAuthority WHERE r.id = :roleId")
//    void updateRoleNameById(Integer roleId, String newAuthority);
}
