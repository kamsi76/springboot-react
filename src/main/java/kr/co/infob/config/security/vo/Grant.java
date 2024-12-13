package kr.co.infob.config.security.vo;

import org.springframework.security.core.GrantedAuthority;

import kr.co.infob.common.database.entity.RoleInfo;

@SuppressWarnings("serial")
public class Grant extends RoleInfo implements GrantedAuthority {

	@Override
	public String getAuthority() {
		return getRoleCd();
	}

}
