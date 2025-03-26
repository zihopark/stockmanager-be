package com.stock.entity;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name="tbl_member")
public class MemberEntity {
	
	@Id
	@Column(name="email",length=50,nullable=false)
	private String email;
	
	@Column(name="username",length=50,nullable=false)
	private String username;
	
	@Column(name="password",length=200,nullable=false)
	private String password;

	@Column(name="telno",length=20,nullable=true) //Telno 만 AES 암호화 진행.
	private String telno;
	
	@Column(name="role",length=20,nullable=false) //기본 USER, 권한 획득 후 MANAGER, 관리자 MASTER
	private String role;
	
	@Column(name="regdate",nullable=false)
	private LocalDateTime regdate;
	
	@Column(name="authkey",length=200, nullable=true)
	private String authkey;
	
	@Column(name="lastlogindate", nullable=true)
	private LocalDateTime lastlogindate;
	
	@Column(name="lastlogoutdate", nullable=true)
	private LocalDateTime lastlogoutdate;
	
	
}
