package kr.co.infob.config.security.service;

import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.ConfigAttribute;
import org.springframework.security.access.SecurityConfig;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.security.web.util.matcher.RequestMatcher;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import kr.co.infob.config.security.mapper.SecurityMapper;
import kr.co.infob.config.security.vo.UrlRoleMapping;
import kr.co.infob.config.security.vo.UserDetailsVo;

@Service
public class SecurityService {

	@Autowired
	private SecurityMapper securityMapper;

	/**
	 * 사용자 정보를 조회 하여 UserDetails에 담는다.
	 * @param username
	 * @return
	 */
	public UserDetailsVo loadUserByUsername(String username) {

		UserDetailsVo user = securityMapper.getUsername(username);
		if( ObjectUtils.isEmpty(user)) {
			throw new UsernameNotFoundException("User not found.");
		}

		return user;
	}

	/**
	 * Database에서 URL에 따른 권한 정보를 호출하여 접근 여부를 확인하는데 사용한다.
	 * @return
	 */
	public LinkedHashMap<RequestMatcher, List<ConfigAttribute>> selectUrlRoleMapping() {

		LinkedHashMap<RequestMatcher, List<ConfigAttribute>> resourcesMap = new LinkedHashMap<RequestMatcher, List<ConfigAttribute>>();

		List<UrlRoleMapping> resultList = securityMapper.selectUrlRoleMapping();
		String preResource = null;
		RequestMatcher presentResource;
		List<ConfigAttribute> configList;

		for(UrlRoleMapping vo : resultList){
			presentResource = new AntPathRequestMatcher(vo.getResrcUrl());

			if(preResource != null && vo.getResrcUrl().equals(preResource)){
				List<ConfigAttribute> preAuthList = resourcesMap.get(presentResource);
				preAuthList.add(new SecurityConfig(vo.getRoleCd()));
			}else{
				configList = new LinkedList<ConfigAttribute>();
				configList.add(new SecurityConfig(vo.getRoleCd()));
				resourcesMap.put(presentResource, configList);
			}

			preResource = vo.getResrcUrl();
		}

		return resourcesMap;
	}

	

}