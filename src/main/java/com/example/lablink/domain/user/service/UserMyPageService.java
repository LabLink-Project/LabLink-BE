package com.example.lablink.domain.user.service;

import com.example.lablink.domain.user.dto.request.MyPageCheckRequestDto;
import com.example.lablink.domain.user.dto.response.UserModifyResponseDto;
import com.example.lablink.domain.user.entity.User;
import com.example.lablink.domain.user.repository.UserRepository;
import com.example.lablink.domain.user.security.UserDetailsImpl;
import com.example.lablink.global.exception.GlobalErrorCode;
import com.example.lablink.global.exception.GlobalException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Service
@RequiredArgsConstructor
public class UserMyPageService {

    private final UserService userService;
    private final PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;
    private final UserMapper userMapper;

    // 비밀번호 확인
    public UserModifyResponseDto checkUser(UserDetailsImpl userDetails, MyPageCheckRequestDto checkRequestDto) {
        User user = userService.getUser(userDetails);
        String myPassword = userService.getUser(userDetails).getPassword();
        String inputPassword = checkRequestDto.getPassword();

        if(!passwordEncoder.matches(inputPassword, myPassword)) {
            throw new GlobalException(GlobalErrorCode.PASSWORD_MISMATCH);
        }
        return new UserModifyResponseDto(user.getUserName(), user.getDateOfBirth());
    }

    // 유저 정보 수정
    // TODO 정보 변경시 이름과 생년월일 등 기존의 정보를 보여줘야 한다면 attribute? 등 사용해야하나 ?
    @Transactional
    public void modifyProfile(UserDetailsImpl userDetails, MyPageCheckRequestDto.UserModifyRequestDto checkRequestDto) {
        User user = userDetails.getUser();
        if(user == null) { throw new GlobalException(GlobalErrorCode.INVALID_TOKEN); }

        userMapper.updateUserModifyDto(checkRequestDto, user);
        userRepository.save(user);
    }

    // 유저 비밀번호 변경
    @Transactional
    public void changePassword(UserDetailsImpl userDetails, MyPageCheckRequestDto checkRequestDto) {
        User user = userDetails.getUser();

        String inputPassword = checkRequestDto.getPassword();
        String myPassword = userService.getUser(userDetails).getPassword();

        if(passwordEncoder.matches(inputPassword, myPassword)) {
            throw new GlobalException(GlobalErrorCode.DUPLICATE_PASSWORD);
        }

        user.setPassword(passwordEncoder.encode(inputPassword));
        userRepository.save(user);
    }

}
