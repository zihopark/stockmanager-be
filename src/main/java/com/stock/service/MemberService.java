package com.stock.service;

import java.util.List;

import org.springframework.data.domain.Page;

import com.stock.dto.MemberDTO;
import com.stock.entity.MemberEntity;
import com.stock.entity.naverProduct.OptionEntity;

public interface MemberService {

	//아이디 중복 체크. 카운터가 0이면 아이디 사용 가능, 1이면 기존 사용 중인 아이디
	public int idCheck(String email);
	
	//사용자 정보 보기
	public MemberDTO memberInfo(String email);
	
	//사용자등록
	public void signup(MemberDTO member);
	
	//사용자 기본 정보 수정
	public void modifyMemberInfo(MemberDTO member);
	
	//사용자 패스워드 수정
	public void modifyMemberPassword(String email,String Password);
	
	//마지막 로그인/로그아웃/패스워드 변경 날짜 등록 하기
	public void lastdateUpdate(String email, String status);
	
	//사용자 자동 로그인을 위한 authkey 등록
	public void authkeyUpdate(MemberDTO member);
	
	//사용자 자동 로그인을 위한 authkey로 사용자 정보 가져 오기 
	public MemberEntity memberInfoByAuthkey(String authkey);
	
	//아이디 찾기
	public String SearchID(MemberDTO member);
	
	//회원 전체 목록 보기 (for Master)
	public List<MemberEntity> getAllMembers();
	
	//사용자 역할 변경
	public void updateRole(MemberDTO member);
}
