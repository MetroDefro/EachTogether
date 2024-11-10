package com.et.eachtogether.user;

import com.et.eachtogether.user.dto.UserRequestDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/user")
public class AuthController {

    private final AuthService authService;

    @PostMapping("/signup")
    public ResponseEntity<String> signUp(@RequestBody UserRequestDto userRequestDto) {
        authService.signUp(userRequestDto);
        return ResponseEntity.ok("회원 가입 완료");
    }

    @PostMapping("/login")
    public ResponseEntity<String> login(@RequestBody UserRequestDto userRequestDto) {
        HttpHeaders headers = authService.login(userRequestDto);
        return ResponseEntity.ok().headers(headers).body("로그인 완료");
    }

    @PostMapping("/logout")
    public ResponseEntity<String> logout() {
        authService.logout();
        return ResponseEntity.ok("로그아웃 완료");
    }

    @PostMapping("/reset-password")
    public ResponseEntity<String> resetPassword(@RequestBody String email) {
        authService.resetPassword(email);
        return ResponseEntity.ok("비밀번호 재설정 이메일 전송 완료");
    }

    @DeleteMapping("/delete")
    public ResponseEntity<String> deleteUser(@RequestBody String email) {
        authService.deleteUser(email);
        return ResponseEntity.ok("회원 탈퇴 완료");
    }
}
