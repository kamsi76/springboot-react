package kr.co.infob.common.database.entity;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class ResrcInfo {

	private String resrcSn;         //리소스시퀀스

	private String resrcPttrn;      //리소스패턴

	private String resrcUrl;        //리소스URL

	private String resrcNm;         //리소스명

	private String resrcDc;         //설명

	private String resrcOrd;        //리소스순서

	private String accessTypeCd;    //접근유코드 : 전체(ALL), 공통(COMM), 메뉴(MENU)

	private String resrcTypeCd;

	private String resrcUseYn;      //리소스사용여부

	private String registerId;      //등록자아이디

	private String registDttm;      //등록일시

	private String updusrId;        //수정자아이디

	private String updtDttm;        //수정일시
}
