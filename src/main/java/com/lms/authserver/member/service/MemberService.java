package com.lms.authserver.member.service;

import com.lms.authserver.global.exception.NotFoundException;
import com.lms.authserver.global.kafka.KafkaAction;
import com.lms.authserver.global.kafka.KafkaMember;
import com.lms.authserver.global.kafka.KafkaProducer;
import com.lms.authserver.global.util.JwtUtil;
import com.lms.authserver.member.dto.EmailVerification;
import com.lms.authserver.member.dto.InfoResponse;
import com.lms.authserver.member.dto.LoginRequest;
import com.lms.authserver.member.dto.SignupRequest;
import com.lms.authserver.member.entity.Member;
import com.lms.authserver.member.repository.MemberRepository;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.http.HttpStatus;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

@Service
@RequiredArgsConstructor
public class MemberService {

    private final MemberRepository repository;
    private final BCryptPasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;
    private final JavaMailSender javaMailSender;
    private final StringRedisTemplate redisTemplate;
    private final KafkaProducer kafkaProducer;


    @Transactional
    public void saveMember(SignupRequest request){
            String encodePassword = passwordEncoder.encode(request.getPassword());
            Member member = repository.save(request.toEntity(encodePassword));

            KafkaMember kafkaMember = KafkaMember.builder()
                    .id(String.valueOf(member.getId()))
                    .email(member.getEmail())
                    .name(member.getName())
                    .phNumber(member.getPhNumber())
                    .year(member.getYear())
                    .studentNumber(member.getStudentNumber())
                    .status(String.valueOf(member.getStatus()))
                    .role(String.valueOf(member.getRole()))
                    .majorList(String.join(",", member.getMajorNames()))
                    .kafkaAction(KafkaAction.CREATE)
                    .build();

            kafkaProducer.signup("member",kafkaMember);
    }

    public void login(LoginRequest request, HttpServletResponse response) {

        String email = request.getEmail();
        String password = request.getPassword();

        // 회원 찾기 (email)
        Member member = repository.findByEmail(email).orElseThrow(
                () -> new NotFoundException("올바른 아이디가 아닙니다.")
        );

        // 회원 찾기 (password)
        if (!passwordEncoder.matches(password, member.getPassword())) {
            throw new NotFoundException("올바른 비밀번호가 아닙니다.");
        }

        String role = String.valueOf(member.getRole());

        System.out.println("로그인 성공");
        issueTokens(member.getId(),role,response);

    }

    public void issueTokens(UUID memberId, String role, HttpServletResponse response){
        String accessToken = jwtUtil.createAccessToken(memberId,role);
        String refreshToken = jwtUtil.createRefreshToken(memberId,role);
        jwtUtil.addJwtToCookie("AccessToken", accessToken, response);
        jwtUtil.addJwtToCookie("RefreshToken", refreshToken, response);
        ValueOperations<String, String> ops = redisTemplate.opsForValue();
        ops.set(String.valueOf(memberId),refreshToken);
    }

    public void reissue(Cookie cookieRefreshToken,HttpServletResponse response) {

        // 쿠키에 담긴 리프레시 토큰 가져와서 멤버 아이디 조회
        // 쿠키 없을시 예외처리
        String refreshTokenValue = cookieRefreshToken.getValue();
        UUID memberId = jwtUtil.getMemberIdFromToken(refreshTokenValue);

        // 멤버 아이디로 redis 에 저장되어 있는 토큰 가져오기
        String redisRefreshToken = redisTemplate.opsForValue().get(String.valueOf(memberId)).substring(7);

        // 토큰값 비교
        if (refreshTokenValue.equals(redisRefreshToken)) {
            System.out.println("올바른 토큰임");

            // 액세스 토큰 재발급
            String role = jwtUtil.getRoleFromToken(redisRefreshToken);
            String accessToken = jwtUtil.createAccessToken(memberId,role);
            jwtUtil.addJwtToCookie("AccessToken", accessToken, response);

        } else{
            System.out.println("토큰 탈취 우려있음");
            System.out.println("1."+refreshTokenValue);
            System.out.println("2."+redisRefreshToken.substring(7));

            // 리프레시 토큰 삭제
            redisTemplate.delete(String.valueOf(memberId));

            // 로그인 다시 시키기
            response.setStatus(HttpStatus.UNAUTHORIZED.value());
        }

    }

    private final long EXPIRATION_SECONDS = 60; // 1 min

    @Transactional
    public void emailVerification(EmailVerification emailVerification) {
        MimeMessage mimeMessage = javaMailSender.createMimeMessage();

        try {
            MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(mimeMessage, false, "UTF-8");
            mimeMessageHelper.setTo(emailVerification.getEmail()); // 메일 수신자
            mimeMessageHelper.setSubject("이메일 인증번호 입니다."); // 메일 제목
            mimeMessageHelper.setText("1648", false); // 메일 본문 내용, HTML 여부
            // 잠시 메일보내기 막음
//             javaMailSender.send(mimeMessage);

            ValueOperations<String, String> ops = redisTemplate.opsForValue();
            ops.set(emailVerification.getEmail(),"1648",EXPIRATION_SECONDS, TimeUnit.SECONDS);
        } catch (MessagingException e) {
            throw new RuntimeException(e);
        }
    }

    public Boolean emailCheck(EmailVerification emailVerification) {
        String redisVerification = Optional.ofNullable(
                redisTemplate.opsForValue().get(emailVerification.getEmail())
        ).orElseThrow( () -> new NotFoundException("이메일 인증하기를 눌러주세요.") );
        return redisVerification.equals(emailVerification.getVerificationNumber());
    }

    public InfoResponse userInfo(Cookie cookieAccessToken) {
        String token = cookieAccessToken.getValue();
        String id = String.valueOf(jwtUtil.getMemberIdFromToken(token));
        String role = jwtUtil.getRoleFromToken(token);
        return new InfoResponse(id,role);
    }

}
