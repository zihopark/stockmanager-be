package com.stock.entity.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.stock.entity.MemberEntity;

import jakarta.transaction.Transactional;

public interface MemberRepository extends JpaRepository<MemberEntity,String>{
	
	public MemberEntity findByAuthkey(String authkey);
	public Optional<MemberEntity> findByUsernameAndTelno(String username, String telno);
	
	@Modifying
	@Transactional
	@Query("UPDATE MemberEntity m SET m.role = :role WHERE m.email = :email")
	public void updateRole(@Param("email") String email, @Param("role") String role);
	
}