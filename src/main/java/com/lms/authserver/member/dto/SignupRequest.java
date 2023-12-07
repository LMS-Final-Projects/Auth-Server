package com.lms.authserver.member.dto;

import com.lms.authserver.member.entity.Member;
import com.lms.authserver.member.entity.MemberRole;
import com.lms.authserver.member.entity.MemberStatus;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.stream.Collectors;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class SignupRequest {

    @NotBlank(message = "비밀번호를 입력해주세요.")
    private String password;
    @NotBlank(message = "이름을 입력해주세요.")
    private String name;
    private List<Long> majorIds;
    @NotBlank(message = "이메일을 입력해주세요.")
    private String email;
    @NotBlank(message = "전화번호를 입력해주세요.")
    private String phNumber;
    private String verificationNumber;
    private MemberStatus status;
    private MemberRole role;

    public Member toEntity(String encodePassword){
        return Member.builder()
                .password(encodePassword)
                .name(name)
                .email(email)
                .phNumber(phNumber)
                .year(2023)
                .studentNumber(2023)
                .status(status)
                .role(role)
                .build();
    }

}
