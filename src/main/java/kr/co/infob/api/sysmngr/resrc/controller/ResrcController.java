package kr.co.infob.api.sysmngr.resrc.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.core.JsonProcessingException;

import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import kr.co.infob.api.sysmngr.resrc.service.ResrcService;
import kr.co.infob.api.sysmngr.resrc.vo.ResrcSearchVo;
import kr.co.infob.api.sysmngr.resrc.vo.ResrcVo;
import kr.co.infob.common.vo.ResponseSelectPerPageVo;

@RestController
@RequestMapping("/api/v1/sysmngr/resrc")
public class ResrcController {

	@Resource(name="resrcService")
	private ResrcService resrcService;

	@PostMapping("/list")
	public ResponseEntity<ResponseSelectPerPageVo<ResrcSearchVo, List<ResrcVo>>> list(HttpServletRequest request, @RequestBody ResrcSearchVo search) throws JsonProcessingException {

		List<ResrcVo> list = resrcService.select(request, search);
		
		ResponseSelectPerPageVo<ResrcSearchVo, List<ResrcVo>> response = ResponseSelectPerPageVo.of(search, list);
		return ResponseEntity.ok().body(response);
	}
}
