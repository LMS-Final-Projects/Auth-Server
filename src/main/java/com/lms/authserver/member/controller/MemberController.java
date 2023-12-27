package com.lms.authserver.member.controller;

import com.lms.authserver.global.response.LmsResponse;
import com.lms.authserver.member.dto.EmailVerification;
import com.lms.authserver.member.dto.InfoResponse;
import com.lms.authserver.member.dto.LoginRequest;
import com.lms.authserver.member.dto.SignupRequest;
import com.lms.authserver.member.service.MemberService;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import java.time.LocalDateTime;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/member")
public class MemberController {

    private final MemberService memberService;

    @PostMapping("/signup")
    public LmsResponse<String> saveMember(@RequestBody @Valid SignupRequest request, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            String errorMessage = bindingResult.getFieldError().getDefaultMessage();
            return new LmsResponse<>(HttpStatus.BAD_REQUEST, "", "유효성 검사 실패", errorMessage, LocalDateTime.now());
        }
        memberService.saveMember(request);
        return new LmsResponse<>(HttpStatus.OK, "", "회원가입 성공", "", LocalDateTime.now());
    }

    @PostMapping("/login")
    public LmsResponse<String> login(@RequestBody LoginRequest request, HttpServletResponse response){
        memberService.login(request,response);
        return new LmsResponse<>(HttpStatus.OK, "", "로그인 성공", "", LocalDateTime.now());
    }

    @GetMapping("/reissue")
    public void reissue(@CookieValue(value = "RefreshToken", required = false) Cookie cookieRefreshToken, HttpServletResponse response){
        memberService.reissue(cookieRefreshToken,response);
    }

    @PostMapping("/send")
    public void emailVerification(@RequestBody EmailVerification emailVerification){
        memberService.emailVerification(emailVerification);
    }

    @PostMapping("/check")
    public LmsResponse<String> emailCheck(@RequestBody EmailVerification emailVerification){
        if(memberService.emailCheck(emailVerification)){
            return new LmsResponse<>(HttpStatus.OK, "true", "인증 성공", "", LocalDateTime.now());
        } else {
            return new LmsResponse<>(HttpStatus.OK, "false", "인증 실패", "올바른 인증번호가 아닙니다.", LocalDateTime.now());
        }
    }

    @PostMapping("/info")
    public LmsResponse<InfoResponse> userInfo(@CookieValue(value = "AccessToken", required = false) Cookie cookieAccessToken){

        InfoResponse infoResponse =  memberService.userInfo(cookieAccessToken);
        return new LmsResponse<>(HttpStatus.OK, infoResponse, "조회 성공", "", LocalDateTime.now());
    }
    
}
