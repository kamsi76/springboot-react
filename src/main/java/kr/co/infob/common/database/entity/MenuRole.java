package kr.co.infob.common.database.entity;

import java.util.Date;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class MenuRole {

	private Long menuSn;        // 메뉴일련번호

	private String roleCd;        // 권한코드

	private String readAuthYn;    // 읽기권한

	private String writeAuthYn;   // 쓰기권한

	private String registerId;    // 등록자아이디

	private Date registDttm;    // 등록일시

	private String updusrId;      // 수정자아이디

	private Date updtDttm;      // 수정일시

}
