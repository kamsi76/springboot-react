package kr.co.infob.config.security.vo;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import kr.co.infob.common.database.entity.UserInfo;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@SuppressWarnings("serial")
@Getter
@RequiredArgsConstructor
public class UserDetailsVo extends UserInfo implements UserDetails {

	private List<GrantedAuthority> authorities;

	private List<Grant> roles;

	@Override
	public String getUsername() {
		return getUserId();
	}

	@Override
	public String getPassword() {
		return getPasswd();
	}

	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {

		if(authorities == null){
			return getRoles()
					.stream()
						.map((role) -> new SimpleGrantedAuthority(role.getRoleCd()))
						.collect(Collectors.toSet());
		}
		return null;
	}

	public void addRole(String roleCode) {
		if (roles == null) {
			roles = new ArrayList<>();
	    }

		Grant grant = new Grant();
		grant.setRoleCd(roleCode);
		roles.add(grant);
	}

	/**
     * 계정이 만료되지 않았는지 여부를 반환합니다.
     * 현재 항상 false를 반환하므로, 모든 계정이 만료된 것으로 처리됩니다.
     *
     * @return boolean
     */
	@Override
	public boolean isAccountNonExpired() {
		return false;
	}

	/**
     * 계정이 잠기지 않았는지 여부를 반환합니다.
     *
     * @return boolean
     */
	@Override
    public boolean isAccountNonLocked() {
        return false;
    }

	/**
     * 자격 증명(비밀번호)이 만료되지 않았는지 여부를 반환합니다.
     *
     * @return boolean
     */
    @Override
    public boolean isCredentialsNonExpired() {
        return false;
    }

    /**
     * 계정이 활성화되어 있는지 여부를 반환합니다.
     *
     * @return boolean
     */
    @Override
    public boolean isEnabled() {
        return false;
    }

}