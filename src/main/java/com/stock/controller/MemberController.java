package com.stock.controller;

import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.stock.dto.MemberDTO;
import com.stock.entity.MemberEntity;
import com.stock.entity.repository.MemberRepository;
import com.stock.service.MemberService;
import com.stock.util.JWTUtil;
import com.stock.util.PasswordMaker;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;

@RestController
@AllArgsConstructor
@Log4j2
@RequestMapping("/member")
public class MemberController {
	
	private final MemberService service;
	private final MemberRepository memberRepository;
	private final BCryptPasswordEncoder pwdEncoder;
	private final JWTUtil jwtUtil;	
	
	//로그인
	@PostMapping("/loginCheck")
	public ResponseEntity<String> postLogIn(MemberDTO loginData,HttpSession session, 
			@RequestParam("autoLogin") String autoLogin, HttpServletRequest request) throws Exception {
		
		String authkey = "";
		String accessToken = "";
		String refreshToken = "";
		String str = "";
		
		System.out.println("LoginCheck 도착!");
		
		//authkey가 클라이언트에 쿠키로 존재할 경우 로그인 과정 없이 세션 생성 후 게시판 목록 페이지로 이동  
		if(autoLogin.equals("PASS")) {		
			if(memberRepository.findByAuthkey(loginData.getAuthkey()) != null) {
				str = "{\"message\":\"good\"}";
				log.info("자동 로그인!");
				return ResponseEntity.ok().body(str);
			} else {
				str = "{\"message\":\"bad\"}";
				log.info("로그인 실패...");
				return ResponseEntity.ok().body(str);
			}				
		}
				
		//아이디 존재 여부 확인
		if(service.idCheck(loginData.getEmail()) == 0) {
			str = "{\"message\":\"ID_NOT_FOUND\"}";
			log.info("아이디가 존재 하지 않음");
			return ResponseEntity.ok().body(str);
		}			
		
		//아이디가 존재하면 읽어온 email로 로그인 정보 가져 오기
		MemberDTO member = service.memberInfo(loginData.getEmail());
		
		//패스워드 확인
		if(!pwdEncoder.matches(loginData.getPassword(),member.getPassword())) {
			str = "{\"message\":\"PASSWORD_NOT_FOUND\"}";
			log.info("패스워드가 틀렸음");
			return ResponseEntity.ok().body(str);
		}
		
		//Form에서 읽은 email, 패스워드 값으로 로그인
		if(autoLogin.equals("NEW")) {
			
			//authkey 생성 및 DB 저장 
			authkey = UUID.randomUUID().toString().replaceAll("-", ""); 
			member.setAuthkey(authkey);
			service.authkeyUpdate(member);
			
		    //토큰 생성을 위해 MemberEntity 객체 생성
		    MemberEntity memberEntity = new MemberEntity();
		    memberEntity.setEmail(member.getEmail());
		    memberEntity.setRole(member.getRole());

		    //generateToken 메서드에 맞게 MemberEntity 객체를 전달
		    accessToken = jwtUtil.generateToken(memberEntity, 1);
		    refreshToken = jwtUtil.generateToken(memberEntity, 5);

		    // JSON 응답 생성
		    str = "{\"message\":\"good\",\"authkey\":\"" + member.getAuthkey() + "\",\"accessToken\":\"" + accessToken + "\",\"refreshToken\":\"" + refreshToken +
		           "\",\"username\":\"" + URLEncoder.encode(member.getUsername(), "UTF-8") + "\",\"role\":\"" + member.getRole() + "\"}";

			//로그인 로그 정보 기록
			service.lastdateUpdate(loginData.getEmail(), "login");
			log.info("아이디/패스워드 로그인");
		} 
		return ResponseEntity.ok().body(str);
	}
	
	//로그아웃
	@GetMapping("/logout")
	public ResponseEntity<?> getLogout(@RequestParam("email") String email) throws Exception {
		//로그아웃 날짜 및 회원 로그 정보 등록
		service.lastdateUpdate(email, "logout");
		return ResponseEntity.ok().build();
	}
	
	//토큰 유효성 검사
	@GetMapping("/validateToken")
	public String getValidate(HttpServletRequest request) throws Exception {
		String token = jwtUtil.getTokenFromAuthorization(request);
		if(token.equals("INVALID_HEADER")) //Authorization Header에 Bearer가 존재하지 않음 
			return "{\"message\":\"bad\"}";
		String jwtCheck = jwtUtil.validateToken(token);
		
		switch(jwtCheck) { //Bearer 내의 JWT의 상태			
			case "VALID_JWT" : return "{\"message\":\"VALID_JWT\", \"email\":\"" + (String)jwtUtil.getDataFromToken(token).get("email") + "\"}";			
			case "EXPIRED_JWT" : return "{\"message\":\"EXPIRED_JWT\"}";
			case "INVALID_JWT":
			case "UNSUPPORTED_JWT":
			case "EMPTY_JWT": return "{\"message\":\"INVALID_JWT\"}";													
		}
		return null;
	}
	
	//아이디 중복 확인
	@PostMapping("/idCheck")
	public ResponseEntity<String> getIdCheck(@RequestParam("email") String email) {	
		String json = service.idCheck(email) == 0 ? "{\"status\":\"good\"}":"{\"status\":\"bad\"}";
		return ResponseEntity.ok().body(json);
	}
	
	//회원 등록 및 기본 정보 수정
	@PostMapping("/signup")
	public ResponseEntity<Map<String,String>> postSignup(MemberDTO member, @RequestParam("kind") String kind,
			@RequestParam(name="imgProfile",required=false) MultipartFile mpr) throws Exception {
		
		//회원 등록
		if(kind.equals("I")) {
			member.setPassword(pwdEncoder.encode(member.getPassword()));
			service.signup(member);	
		}
		
		//회원 수정
		if(kind.equals("U")) {
			service.modifyMemberInfo(member);
		}
		
		Map<String,String> data = new HashMap<>();
		data.put("status", "good");
		data.put("username", URLEncoder.encode(member.getUsername(),"UTF-8"));
		
		return ResponseEntity.ok().body(data);
		
	}
	
	//사용자 정보 보기
	@GetMapping("/memberInfo")
	public ResponseEntity<MemberDTO> getMemberInfo(@RequestParam("email") String email) {		
		return ResponseEntity.ok().body(service.memberInfo(email));
	}

	//회원 패스워드 변경
	@PostMapping("/modifyMemberPassword")
	public ResponseEntity<String> postMemberPasswordModify(@RequestParam("old_password") String old_password, 
				@RequestParam("new_password") String new_password, @RequestParam("email") String email) throws Exception { 
		
		String json = "";
		//이전 패스워드가 제대로 된 패스워드인지 확인
		if(!pwdEncoder.matches(old_password, service.memberInfo(email).getPassword())) {
			json = "{\"message\":\"PASSWORD_NOT_FOUND\"}";
			return ResponseEntity.ok().body(json); 
		}
		
		//신규 패스워드로 수정
		service.modifyMemberPassword(email,new_password);
		
		//마지막 패스워드 변경일 등록
		service.lastdateUpdate(email, "password");
		json = "{\"message\":\"good\"}";
		return ResponseEntity.ok().body(json);
	}
	
	
	//아이디 찾기
	@PostMapping("/searchID")
	public ResponseEntity<String> postSearchID(MemberDTO member) {
		
		String email = service.SearchID(member) == null?"ID_NOT_FOUND":service.SearchID(member);	
		String json = "{\"message\":\"" + email + "\"}";
		return ResponseEntity.ok().body(json);
	}
	
	
	//패스워드 임시 발급
	@PostMapping("/searchPassword")
	public ResponseEntity<String> postSearchPassword(MemberDTO member) throws Exception{
		//아이디 존재 여부 확인
		String json = "";
		if(service.idCheck(member.getEmail()) == 0) {
			json = "{\"status\":\"ID_NOT_FOUND\"}";
			return ResponseEntity.ok().body(json);
		}
		//TELNO 확인
		if(!service.memberInfo(member.getEmail()).getTelno().equals(member.getTelno())) {
			json = "{\"status\":\"TELNO_NOT_FOUND\"}";
			return ResponseEntity.ok().body(json);
		}					
		//임시 패스워드 생성	
		PasswordMaker pMaker = new PasswordMaker();
		String rawTempPW = pMaker.tempPasswordMaker();
		//임시 패스워드로 패스워드 수정
		service.modifyMemberPassword(member.getEmail(),rawTempPW);
		System.out.println("임시 패스워드 :"+rawTempPW);
		json = "{\"status\":\"good\",\"password\":\"" + rawTempPW + "\"}";
		return ResponseEntity.ok().body(json);
	}

	
}
