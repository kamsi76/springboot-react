package kr.co.infob.common.vo;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@Builder
public class ResponseSelectPerPageVo<S extends CommonSearchVo, D> {

	private S search;	//검색 조건

	private D data;		//검색된 데이터

	// 정적 팩토리 메서드
    public static <S extends CommonSearchVo, D> ResponseSelectPerPageVo<S, D> of(S search, D data) {
        return ResponseSelectPerPageVo.<S, D>builder()
                .search(search)
                .data(data)
                .build();
    }
}
