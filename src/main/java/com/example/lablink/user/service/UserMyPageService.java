package com.example.lablink.user.service;

import com.example.lablink.user.dto.request.MyPageCheckRequestDto;
import com.example.lablink.user.dto.response.UserModifyResponseDto;
import com.example.lablink.user.entity.User;
import com.example.lablink.user.exception.UserErrorCode;
import com.example.lablink.user.exception.UserException;
import com.example.lablink.user.repository.UserRepository;
import com.example.lablink.user.security.UserDetailsImpl;
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
            throw new UserException(UserErrorCode.PASSWORD_MISMATCH);
        }
        return new UserModifyResponseDto(user.getUserName(), user.getDateOfBirth());
    }

    // 유저 정보 수정
    // TODO 정보 변경시 이름과 생년월일 등 기존의 정보를 보여줘야 한다면 attribute? 등 사용해야하나 ?
    @Transactional
    public void modifyProfile(UserDetailsImpl userDetails, MyPageCheckRequestDto.UserModifyRequestDto checkRequestDto) {
        User user = userDetails.getUser();
        if(user == null) { throw new UserException(UserErrorCode.INVALID_TOKEN); }

        userMapper.updateUserModifyDto(checkRequestDto, user);

//        if (checkRequestDto.getUserName() != null && !checkRequestDto.getUserName().isBlank()) {
//            user.setUserName(checkRequestDto.getUserName());
//        }
//
//        if (checkRequestDto.getDateOfBirth() != null) {
//            user.setDateOfBirth(checkRequestDto.getDateOfBirth());
//        }
//
//        if (checkRequestDto.getUserGender() != null && !checkRequestDto.getUserGender().isBlank()) {
//            user.setUserGender(checkRequestDto.getUserGender());
//        }
//
//        if (checkRequestDto.getUserPhone() != null && !checkRequestDto.getUserPhone().isBlank()) {
//            user.getUserinfo().setUserPhone(checkRequestDto.getUserPhone());
//        }
//
//        if (checkRequestDto.getUserAddress() != null && !checkRequestDto.getUserAddress().isBlank()) {
//            user.getUserinfo().setUserAddress(checkRequestDto.getUserAddress());
//        }
        userRepository.save(user);
    }

    // 유저 비밀번호 변경
    @Transactional
    public void changePassword(UserDetailsImpl userDetails, MyPageCheckRequestDto checkRequestDto) {
        User user = userDetails.getUser();

        String inputPassword = checkRequestDto.getPassword();
        String myPassword = userService.getUser(userDetails).getPassword();

        if(passwordEncoder.matches(inputPassword, myPassword)) {
            throw new UserException(UserErrorCode.DUPLICATE_PASSWORD);
        }

        user.setPassword(passwordEncoder.encode(inputPassword));
        userRepository.save(user);
    }

}
