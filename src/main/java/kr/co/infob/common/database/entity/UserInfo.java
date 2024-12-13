package kr.co.infob.common.database.entity;

import java.util.Date;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@Getter @Setter
@RequiredArgsConstructor
public class UserInfo {

	private String userId;

	private String passwd;

	private String userNm;

	private String cttpc;

	private String email;

	private Date registDttm;

	private String updusrId;

	private Date updtDttm;

}