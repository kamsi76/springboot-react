package kr.co.infob.config.security.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import kr.co.infob.config.security.vo.UrlRoleMapping;
import kr.co.infob.config.security.vo.UserDetailsVo;

@Mapper
public interface SecurityMapper {

	/**
	 * 사용자 정보 조회
	 * @param username
	 * @return
	 */
	UserDetailsVo getUsername(String username);

	/**
	 * URL에 따른 롤정보 조회
	 * @return
	 */
	List<UrlRoleMapping> selectUrlRoleMapping();

}
