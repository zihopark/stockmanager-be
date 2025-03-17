package com.stock.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.stock.dto.MemberDTO;
import com.stock.entity.MemberEntity;
import com.stock.service.MemberService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RestController
@Slf4j
@RequiredArgsConstructor
@RequestMapping("/master")
public class MasterController {

	public final MemberService memberService;
	
	//전체 회원 목록
	@GetMapping("/memberList")
	public ResponseEntity<List<MemberDTO>> getMemberList() {		
		List<MemberEntity> memberList = memberService.getAllMembers();
		
		//MemberEntity => MemberDTO 변환
		List<MemberDTO> memberDTOList = memberList.stream()
				.map(member -> {
					MemberDTO memberDTO = new MemberDTO();
					memberDTO.setEmail(member.getEmail());
					memberDTO.setLastlogindate(member.getLastlogindate());
					memberDTO.setLastlogoutdate(member.getLastlogoutdate());
					memberDTO.setRegdate(member.getRegdate());
					memberDTO.setRole(member.getRole());
					memberDTO.setTelno(member.getTelno());
					memberDTO.setUsername(member.getUsername());
					
					return memberDTO;
				})
	            .collect(Collectors.toList());
		
		 return ResponseEntity.ok(memberDTOList);
	}

	//회원 역할 업데이트
	@PostMapping("/updateRole")
	public ResponseEntity<?> postUpdateRole(@RequestBody List<MemberDTO> memberDTOList) {
	    try {
	    	System.out.println("List 받은 값은" + memberDTOList );
	        for (MemberDTO memberDTO : memberDTOList) {
	        	System.out.println("각각 받은 값은" + memberDTO );
	            memberService.updateRole(memberDTO);
	        }
	        
	        Map<String, Object> response = new HashMap<>();
	        response.put("success", true);
	        response.put("message", "Roles are updated successfully");
	        return ResponseEntity.status(HttpStatus.OK).body(response);
	        
	    } catch (Exception e) {
	    	e.printStackTrace(); // 전체 스택 트레이스 출력
	        Map<String, Object> errorResponse = new HashMap<>();
	        errorResponse.put("success", false);
	        errorResponse.put("message", "Failed to update roles: " + e.getMessage());
	        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
	    }
	}

}
