package kr.co.infob.common.database.entity;

import java.util.Date;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class RoleInfo {

	private String roleCd;

	private String roleNm;

	private String useYn;

	private String registerId;

	private Date registDttm;

	private String updusrId;

	private Date updtDttm;

}
