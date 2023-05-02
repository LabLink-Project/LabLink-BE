package com.example.lablink.user.service;

import com.example.lablink.jwt.JwtUtil;

import com.example.lablink.user.dto.request.LoginRequestDto;
import com.example.lablink.user.dto.request.SignupRequestDto;
import com.example.lablink.user.dto.request.UserEmailCheckRequestDto;
import com.example.lablink.user.dto.request.UserNickNameRequestDto;
import com.example.lablink.user.dto.response.MyLabResponseDto;
import com.example.lablink.user.entity.User;

import com.example.lablink.user.entity.UserInfo;
import com.example.lablink.user.entity.UserRoleEnum;
import com.example.lablink.user.exception.UserErrorCode;
import com.example.lablink.user.exception.UserException;
import com.example.lablink.user.repository.UserRepository;
import com.example.lablink.user.security.UserDetailsImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final TermsService termsService;
    private final JwtUtil jwtUtil;
    private final UserInfoService userInfoService;
    private final EntityManager em;

    // 유저 회원가입
    @Transactional
    public String signup(SignupRequestDto signupRequestDto) {
        String email = signupRequestDto.getEmail();
        String password = passwordEncoder.encode(signupRequestDto.getPassword());

        // 가입 이메일 중복 확인
        if (userRepository.existsByEmail(email)) {
            throw new UserException(UserErrorCode.DUPLICATE_EMAIL);
        }

        // 가입 닉네임 중복 확인
        if (userRepository.existsByNickName(signupRequestDto.getNickName())) {
            throw new UserException(UserErrorCode.DUPLICATE_NICK_NAME);
        }

        // 필수 약관 동의
        if(!signupRequestDto.isAgeCheck() || !signupRequestDto.isTermsOfServiceAgreement() || !signupRequestDto.isPrivacyPolicyConsent() || !signupRequestDto.isSensitiveInfoConsent()) {
            throw new UserException(UserErrorCode.NEED_AGREE_REQUIRE_TERMS);
        }
        // 유저 저장 및 유저를 약관에 저장시킴 -> 약관을 유저에 저장시키면 유저를 불러올때마다 약관이 불려와 무거움
        // userinfo는 회원가입할 때 받지 않음.
        UserInfo userInfo = userInfoService.saveUserInfo(signupRequestDto);
        User user = userRepository.save(new User(signupRequestDto, password, userInfo, UserRoleEnum.USER));
        termsService.saveTerms(signupRequestDto, user);
        return "회원가입 완료.";
    }

    // 유저 로그인
    @Transactional
    public String login(LoginRequestDto loginRequestDto, HttpServletResponse response) {
        String email = loginRequestDto.getEmail();
        String password = loginRequestDto.getPassword();

        // 이메일 존재, 일치 여부
        User user = userRepository.findByEmail(email).orElseThrow(() -> new UserException(UserErrorCode.EMAIL_NOT_FOUND));

        // 비밀번호 일치 여부
        if (!passwordEncoder.matches(password, user.getPassword())) {
            throw new UserException(UserErrorCode.PASSWORD_MISMATCH);
        }

        response.addHeader(JwtUtil.AUTHORIZATION_HEADER, jwtUtil.createUserToken(user));
        return "로그인 완료.";
        // CSRF, JWT토큰 생성
//        CsrfToken userCsrfToken = csrfTokenRepository.generateToken(request);
//        String userToken = jwtUtil.createUserToken(user);

        // 쿠키 생성 및 JWT토큰 추가
//        Cookie cookie = new Cookie("Authorization", userToken);
//        cookie.setMaxAge(60 * 60 * 24); // 쿠키 유효 기간 (1일)
//        cookie.setPath("/"); // 전제api가 쿠키에 액세스 가능
//        cookie.setHttpOnly(true); // XSS공격 방지 (악성코드?)
////        cookie.setSecure(true); // HTTPS 사용 시 설정 (쿠키가 보안되지 않은 연결을 통해 전송되는 경우 탈취 방지)
//        response.addCookie(cookie);

        // 쿠키 생성 및 CSRF토큰 추가
//        Cookie csrfCookie = new Cookie("XSRF-TOKEN", userCsrfToken.getToken());
//        csrfCookie.setMaxAge(60 * 60 * 24); // 쿠키 유효 기간 (1일)
//        csrfCookie.setPath("/"); // 전제api가 쿠키에 액세스 가능
//        csrfCookie.setHttpOnly(true); // XSS공격 방지 (악성코드?)
////        csrfCookie.setSecure(true); // HTTPS 사용 시 설정 (쿠키가 보안되지 않은 연결을 통해 전송되는 경우 탈취 방지)
//        response.addCookie(csrfCookie);

//        // 세션 쿠키 생성 및 추가 > websecurity 수정 필요
//        HttpSession session = request.getSession(true);
//        session.setAttribute("Authorization", token);
    }

    // 유저 이메일 중복 체크
    @Transactional(readOnly = true)
    public String emailCheck(UserEmailCheckRequestDto userEmailCheckRequestDto) {
        if(userRepository.existsByEmail(userEmailCheckRequestDto.getEmail())) {
            throw new UserException(UserErrorCode.DUPLICATE_EMAIL);
        }
        return "사용 가능합니다.";
    }

    // 유저 닉네임 중복 확인
    public String nickNameCheck(UserNickNameRequestDto userNickNameRequestDto) {
        if(userRepository.existsByNickName(userNickNameRequestDto.getNickName())) {
            throw new UserException(UserErrorCode.DUPLICATE_NICK_NAME);
        }
        return "사용 가능합니다.";
    }

    // 인증 유저 가져오기
    public User getUser(UserDetailsImpl userDetails){
        return  userRepository.findById(userDetails.getUser().getId()).orElseThrow(
                ()->new UserException(UserErrorCode.USER_NOT_FOUND));
    }

    // 회원 탈퇴
    @Transactional
    public String deleteUser(UserDetailsImpl userDetails, HttpServletResponse response) {
////        북마크 제거
//        bookmarkService.findAllByMyBookmark(userDetails.getUser()).forEach(bookmarkService::deleteAllBookmark);
////        신청서 삭제
//        applicationService.findAllByMyApplication(userDetails.getUser()).forEach(applicationService::deleteApplication);
////        약관 삭제
//        termsService.deleteTerms(userDetails.getUser());
////        유저 삭제
//        userRepository.delete(userDetails.getUser());
//
        userRepository.deleteUserAndData(userDetails.getUser().getId());
//        로그아웃 (헤더 null값 만들기)
        response.setHeader(JwtUtil.AUTHORIZATION_HEADER, null);
        return "탈퇴 완료.";
    }

    // 내 실험 관리 - 신청한 목록
    // TODO 쿼리 사용 & 쿼리 x 성능차이 확인
    // TODO User 권한 없으면 한번에 예외 발생 처리 - 현재 각 메서드별 처리
    @Transactional
    public List<MyLabResponseDto> getMyLabs(UserDetailsImpl userDetails) {
        if(userDetails == null || userDetails.equals(" ")) {
            throw new UserException(UserErrorCode.INVALID_TOKEN);
        }
        /*내가 작성한 신청서를 찾아 리스트에 넣는다*/
//        List<Application> applcations = applicationService.findAllByMyApplication(userDetails.getUser());
//        List<MyLabResponseDto> myLabs = new ArrayList<>();
//        for (Application applcation : applcations) {
//            Study study = getStudyService.getStudy(applcation.getStudyId());
//            myLabs.add(new MyLabResponseDto(study, applcation.getApprovalStatusEnum(), applcation.getApplicationViewStatusEnum()));
//        }
//        return myLabs;

        // 내가 신청한 목록
        // 1. application에서 user로 찾아온다
        // 2. 스터디 정보와 어플리케이션 정보를 찾아서 responseDto에 넣어주려고 한듯 ?
        // db를 두번 찌르지 않기 위해서 이 query를 썻나 ?
        TypedQuery<MyLabResponseDto> query = em.createQuery(
            "SELECT new com.example.lablink.user.dto.response.MyLabResponseDto(s, a.applicationViewStatusEnum, a.approvalStatusEnum) " +
                "FROM Study s INNER JOIN Application a ON s.id = a.studyId " +
                "WHERE a.user = :user", MyLabResponseDto.class);
        query.setParameter("user", userDetails.getUser());

        List<MyLabResponseDto> myLabs = query.getResultList();

        return myLabs;
    }

    // User 찾기
    public User findUser(Long userId) {
        return userRepository.findById(userId).orElseThrow(() -> new UserException(UserErrorCode.USER_NOT_FOUND));
    }

    public User getUserByNickname(String nickName) {
        return userRepository.findByNickName(nickName).orElseThrow(() -> new UserException(UserErrorCode.USER_NOT_FOUND));
    }
}
