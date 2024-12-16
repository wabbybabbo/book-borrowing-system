package org.example.admin.pojo.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Pattern;
import lombok.Data;
import org.example.common.constant.GenderConstant;
import org.example.common.constant.MessageConstant;
import org.example.common.constant.RegexpConstant;

import static io.swagger.v3.oas.annotations.media.Schema.RequiredMode.REQUIRED;

@Data
@Schema(description = "新建用户信息时传递的数据模型")
public class CreateUserDTO {

    @Schema(description = "用户名", pattern = RegexpConstant.USER_NAME, requiredMode = REQUIRED, example = "guest")
    @Pattern(regexp = RegexpConstant.USER_NAME, message = MessageConstant.INVALID_USER_NAME)
    private String name;

    @Schema(description = "账号", pattern = RegexpConstant.ACCOUNT, requiredMode = REQUIRED, example = "guest")
    @Pattern(regexp = RegexpConstant.ACCOUNT, message = MessageConstant.INVALID_ACCOUNT)
    private String account;

    @Schema(description = "密码", pattern = RegexpConstant.PASSWORD, requiredMode = REQUIRED, example = "guest")
    @Pattern(regexp = RegexpConstant.PASSWORD, message = MessageConstant.INVALID_PASSWORD)
    private String password;

    @Schema(description = "性别", pattern = RegexpConstant.GENDER, example = GenderConstant.MALE)
    @Pattern(regexp = RegexpConstant.GENDER, message = MessageConstant.INVALID_GENDER)
    private String gender;

    @Schema(description = "电话号码", example = "12345678901")
    @Pattern(regexp = RegexpConstant.PHONE, message = MessageConstant.INVALID_PHONE)
    private String phone;

    @Schema(description = "邮箱", example = "example@qq.com")
    @Email(regexp = RegexpConstant.EMAIL, message = MessageConstant.INVALID_EMAIL)
    private String email;

    @Schema(description = "联系地址", example = "广东省广州市海珠区")
    private String address;

}
