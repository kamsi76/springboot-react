package kr.co.infob.api.sysmngr.resrc.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import kr.co.infob.api.sysmngr.resrc.vo.ResrcSearchVo;
import kr.co.infob.api.sysmngr.resrc.vo.ResrcVo;

@Mapper
public interface ResrcMapper {

	/**
	 * 검색조건에 해당하는 전체 목록 수 조회
	 * @param search
	 * @return
	 */
	int getCount(ResrcSearchVo search);

	/**
	 * 검색조건에 해당하는 전체 목록
	 * @param search
	 */
	List<ResrcVo> select(ResrcSearchVo search);



}
