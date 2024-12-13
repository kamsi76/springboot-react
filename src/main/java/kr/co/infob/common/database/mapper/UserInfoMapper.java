package kr.co.infob.common.database.mapper;

import org.apache.ibatis.annotations.Mapper;

import kr.co.infob.common.database.entity.UserInfo;

@Mapper
public interface UserInfoMapper {

	/**
	 * 사용자 정보 등록
	 * @param user
	 */
	void insert(UserInfo user);


	void update(UserInfo user);

}
