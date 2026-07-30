package com.panonit.user_service.mapper;


import com.panonit.user_service.dto.CreateUserRequestDto;
import com.panonit.user_service.dto.CreateUserResponseDto;
import com.panonit.user_service.dto.GetUserResponseDto;
import com.panonit.user_service.dto.UpdateUserResponseDto;
import com.panonit.user_service.entity.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface UserMapper {

    @Mapping(source = "notifications", target = "alerting")
    User toEntity(CreateUserRequestDto createUserRequestDto);

    @Mapping(source = "alerting", target = "notifications")
    CreateUserResponseDto toCreateUserResponseDto(User user);

    @Mapping(source = "alerting", target = "notifications")
    UpdateUserResponseDto toUpdateUserResponseDto(User user);

    @Mapping(source = "alerting", target = "notifications")
    GetUserResponseDto toGetUserResponseDto(User user);
}
