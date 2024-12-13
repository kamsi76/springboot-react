package kr.co.infob.common.database.entity;

import java.util.Date;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class MenuInfo {

	private Long menuSn;          // 메뉴id

	private Long upperMenuSn;     // 부모메뉴id

	private Long resrcSn;         // 리소스일련번호

	private String menuNm;        // 메뉴명

	private String menuUrl;       // 메뉴url

	private Integer menuLevel;    // 메뉴레벨

	private String popupYn;       // 팝업창여부

	private String useYn;         // 사용여부

	private String outptYn;       // 보여주기여부

	private Integer menuOrd;      // 순번

	private String registerId;    // 등록자아이디

	private Date registDttm;      // 등록일시

	private String updusrId;      // 수정자아이디

	private Date updtDttm;        // 수정일시

}
