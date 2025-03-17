package com.stock.dto;

import java.time.LocalDateTime;

import com.stock.entity.MemberEntity;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class MemberDTO {
	
	private String email;
	private String username;
	private String password;
	private String telno;	
	private String role;
	private LocalDateTime regdate;
	private String authkey;
	private LocalDateTime lastlogindate;
	private LocalDateTime lastlogoutdate;
	
	public MemberDTO(MemberEntity memberEntity) {
		
		this.email = memberEntity.getEmail();
		this.username = memberEntity.getUsername();
		this.password = memberEntity.getPassword();
		this.telno = memberEntity.getTelno();
		this.role = memberEntity.getRole();
		this.regdate = memberEntity.getRegdate();
		this.lastlogindate = memberEntity.getLastlogindate();
		this.lastlogoutdate = memberEntity.getLastlogoutdate();
		this.authkey = memberEntity.getAuthkey();
				
	}
	
	public MemberEntity dtoToEntity(MemberDTO memberDTO) {
		
		MemberEntity memberEntity = MemberEntity.builder()
											.email(memberDTO.getEmail())
											.username(memberDTO.getUsername())
											.password(memberDTO.getPassword())
											.telno(memberDTO.getTelno())
											.role(memberDTO.getRole())
											.regdate(memberDTO.getRegdate())
											.lastlogindate(memberDTO.getLastlogindate())
											.lastlogoutdate(memberDTO.getLastlogoutdate())
											.authkey(memberDTO.getAuthkey())
											.build();
		return memberEntity;
	}
	
	@Override
	public String toString() {
	    return "MemberDTO{" +
	           "email='" + email + '\'' +
	           ", username='" + username + '\'' +
	           ", password='" + password + '\'' +
	           ", telno='" + telno + '\'' +
	           ", role='" + role + '\'' +
	           ", regdate=" + regdate +
	           ", authkey='" + authkey + '\'' +
	           ", lastlogindate=" + lastlogindate +
	           ", lastlogoutdate=" + lastlogoutdate +
	           '}';
	}

	
}
