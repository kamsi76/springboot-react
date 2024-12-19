package kr.co.infob.common.vo;

import lombok.Getter;
import lombok.Setter;

@Setter @Getter
public class CommonSearchVo {

	private String searchType;

	private String searchText;

	private Pagination pagination;

	@SuppressWarnings("unused")
	private int page;

	@SuppressWarnings("unused")
	private int totalRecord;

	public CommonSearchVo() {
		pagination = new Pagination();
		pagination.setPage(1);
		pagination.setRecordPerPage(5);
	}

	public int getTotalRecord() {
		return this.pagination.getTotalRecord();
	}

	public void setTotalRecord(int totalRecord) {
		this.pagination.setTotalRecord(totalRecord);
	}

	public void setPage(int page) {
		this.pagination.setPage(page);
	}

	public int getPage() {
		return this.pagination.getPage();
	}

}
