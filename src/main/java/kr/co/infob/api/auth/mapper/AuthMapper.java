package kr.co.infob.api.auth.mapper;

import org.apache.ibatis.annotations.Mapper;

import kr.co.infob.api.auth.vo.UserVo;

@Mapper
public interface AuthMapper {

	/**
	 * 사용자 정보 조회
	 * @param userId
	 * @return
	 */
	public UserVo getUser(String userId);
}
