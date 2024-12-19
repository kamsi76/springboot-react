package kr.co.infob.api.sysmngr.resrc.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import jakarta.servlet.http.HttpServletRequest;
import kr.co.infob.api.sysmngr.resrc.mapper.ResrcMapper;
import kr.co.infob.api.sysmngr.resrc.vo.ResrcSearchVo;
import kr.co.infob.api.sysmngr.resrc.vo.ResrcVo;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
@Slf4j
public class ResrcService {

	private final ResrcMapper resrcMapper;

	public List<ResrcVo> select(HttpServletRequest request, ResrcSearchVo search) {

		List<ResrcVo> list = null;

		int total = resrcMapper.getCount(search );
		if( total > 0 ) {
			search.setTotalRecord(total);
			list = resrcMapper.select(search);
		}

		return list;
	}

}
