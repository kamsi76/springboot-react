package kr.co.infob.api.auth.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import kr.co.infob.api.auth.service.AuthService;
import kr.co.infob.api.auth.vo.UserVo;
import kr.co.infob.common.database.entity.UserInfo;

@RestController
@RequestMapping("/api/v1")
public class AuthController {

	@Resource(name="authService")
	private AuthService authService;

	@PostMapping("/auth/signup")
	public ResponseEntity<?> signup() {
		return ResponseEntity.ok("Sign UP!!!!!!");
	}

	@PostMapping("/auth/signin")
	public ResponseEntity<?> signin(HttpServletResponse response, @RequestBody UserInfo userInfo) {
		UserVo user = authService.login(userInfo);

		return ResponseEntity.ok().body(user);
	}

	@GetMapping("/mypage")
	public ResponseEntity<?> mypage() {
		return ResponseEntity.ok("MyPage!!!!!!");
	}

	@PostMapping("/auth/authorize")
	public ResponseEntity<?> authroize(HttpServletRequest request, @RequestBody String path) {
		return ResponseEntity.ok().body(authService.authroize(request, path));
	}

}
