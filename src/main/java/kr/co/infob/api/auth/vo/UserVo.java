package kr.co.infob.api.auth.vo;

import kr.co.infob.common.database.entity.UserInfo;
import kr.co.infob.config.security.vo.TokenVo;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserVo extends UserInfo {
	private TokenVo token;
}