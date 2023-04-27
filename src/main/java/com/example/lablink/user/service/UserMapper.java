package com.example.lablink.user.service;

import com.example.lablink.user.dto.request.MyPageCheckRequestDto;
import com.example.lablink.user.entity.User;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValueCheckStrategy;

@Mapper(componentModel = "spring", nullValueCheckStrategy = NullValueCheckStrategy.ALWAYS)
// coponent주석을달며 spring bean이 생성된다. 모든값의 null 체크를 한다.
public interface UserMapper {

    // dto에서 null값이 아닌값으로 User를 업데이트
    void updateUserModifyDto(MyPageCheckRequestDto.UserModifyRequestDto checkRequestDto, @MappingTarget User user);
}
