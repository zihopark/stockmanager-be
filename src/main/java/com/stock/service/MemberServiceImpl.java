package com.stock.service;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.stock.dto.MemberDTO;
import com.stock.entity.MemberEntity;
import com.stock.entity.repository.MemberRepository;
import com.stock.util.AES256Util;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class MemberServiceImpl implements MemberService{

	private final MemberRepository memberRepository;
	private final BCryptPasswordEncoder pwdEncoder; 
    private final AES256Util aesUtil;
	
	//아이디 중복 체크. 카운터가 0이면 아이디 사용 가능, 1이면 기존 사용 중인 아이디
	@Override
	public int idCheck(String email) {
		return memberRepository.findById(email).isEmpty()?0:1;
	}
	
	//사용자 정보 가져 오기
	@Override
	public MemberDTO memberInfo(String email) {
		MemberDTO dto = memberRepository.findById(email).map(member -> new MemberDTO(member)).get();
	    try {
	        if (dto.getTelno() != null) {
	            dto.setTelno(aesUtil.decrypt(dto.getTelno()));
	        }
	    } catch (Exception e) {
	        throw new RuntimeException("전화번호 복호화 실패", e);
	    }
	    return dto;
	}

	//사용자등록
	@Override
	public void signup(MemberDTO member) {
		member.setRegdate(LocalDateTime.now());
	    member.setRole("USER");

	    try {
	        if (member.getTelno() != null) {
	            member.setTelno(aesUtil.encrypt(member.getTelno()));
	        }
	    } catch (Exception e) {
	        throw new RuntimeException("전화번호 암호화 실패", e);
	    }

	    memberRepository.save(member.dtoToEntity(member));	
	}
	
	//사용자 기본 정보 수정
	public void modifyMemberInfo(MemberDTO member) {
		MemberEntity memberEntity = memberRepository.findById(member.getEmail()).get();
	    memberEntity.setUsername(member.getUsername());
	    try {
	        if (member.getTelno() != null) {
	            memberEntity.setTelno(aesUtil.encrypt(member.getTelno()));
	        }
	    } catch (Exception e) {
	        throw new RuntimeException("전화번호 암호화 실패", e);
	    }
	    memberRepository.save(memberEntity);
	}
	
	//사용자 패스워드 수정
	@Override
	public void modifyMemberPassword(String email,String Password) {
		MemberEntity memberEntity = memberRepository.findById(email).get();
		memberEntity.setPassword(pwdEncoder.encode(Password));
		memberRepository.save(memberEntity);
	}
	
	//마지막 로그인/로그아웃 등록 하기
	@Override
	public void lastdateUpdate(String email,String status) {
		MemberEntity memberEntity = memberRepository.findById(email).get();
		
		switch(status) {
			case "login" : memberEntity.setLastlogindate(LocalDateTime.now());
					 	   break;
			case "logout" : memberEntity.setLastlogoutdate(LocalDateTime.now());
			                break;         
		}		
		memberRepository.save(memberEntity);
	}
	
	
	//사용자 자동 로그인을 위한 authkey 등록
	@Override
	public void authkeyUpdate(MemberDTO member) {
		MemberEntity memberEntity = memberRepository.findById(member.getEmail()).get();
		memberEntity.setAuthkey(member.getAuthkey());
		memberRepository.save(memberEntity);
	}
	
	//사용자 자동 로그인을 위한 authkey로 사용자 정보 가져 오기 
	@Override
	public MemberEntity memberInfoByAuthkey(String authkey) {
		return memberRepository.findByAuthkey(authkey);
	}
	
	//아이디 찾기
	@Override
	public String SearchID(MemberDTO member) {
		try {
	        String encryptedTelno = aesUtil.encrypt(member.getTelno());
	        return memberRepository.findByUsernameAndTelno(member.getUsername(), encryptedTelno)
	                .map(m -> m.getEmail())
	                .orElse("ID_NOT_FOUND");
	    } catch (Exception e) {
	        throw new RuntimeException("전화번호 암호화 실패", e);
	    }	 
	}	

	//회원 전체 목록 보기 (for Master)
	@Override	
	public List<MemberEntity> getAllMembers(){
		return memberRepository.findAll();
	}
	
	//사용자 역할 변경
	@Transactional
	@Override	
	public void updateRole(MemberDTO member) {
		memberRepository.updateRole(member.getEmail(), member.getRole());
	}
}
