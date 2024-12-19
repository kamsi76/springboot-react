package kr.co.infob.common.vo;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class Pagination {

	private int page;

	private int recordPerPage;

	@SuppressWarnings("unused")
	private int totalPage;

	private int totalRecord;

	private int firstRecordIndex;

	private int lastRecordIndex;

	public int getTotalPage() {
		return ((getTotalRecord() - 1) / getRecordPerPage()) + 1;
	}


	public int getFirstRecordIndex() {
		firstRecordIndex = (getPage() - 1) * getRecordPerPage();
		return firstRecordIndex;
	}

	public int getLastRecordIndex() {
		lastRecordIndex = getPage() * getRecordPerPage();
		return lastRecordIndex;
	}
}
