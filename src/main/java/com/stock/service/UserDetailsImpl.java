package com.stock.service;

import java.util.ArrayList;
import java.util.Collection;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import com.stock.entity.MemberEntity;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@SuppressWarnings("serial")
@Getter
@RequiredArgsConstructor
public class UserDetailsImpl implements UserDetails {

	private final MemberEntity member;
	
	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
	    Collection<GrantedAuthority> collection = new ArrayList<>();
	    collection.add(new SimpleGrantedAuthority("ROLE_" + member.getRole())); // "ROLE_" 접두사 추가
	    return collection;
	}

    @Override
    public String getPassword() {
        return member.getPassword();
    }

    @Override
    public String getUsername() {
        return member.getEmail().toString();
    }


    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}
