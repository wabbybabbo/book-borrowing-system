package org.example.admin.pojo.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Data;
import org.example.common.constant.GenderConstant;
import org.example.common.constant.MessageConstant;
import org.example.common.constant.RegexpConstant;

import static io.swagger.v3.oas.annotations.media.Schema.RequiredMode.REQUIRED;

@Data
@Schema(description = "新建用户信息时传递的数据模型")
public class CreateUserDTO {

    @NotBlank(message = MessageConstant.FIELD_NOT_BLANK)
    @Pattern(regexp = RegexpConstant.USER_NAME, message = MessageConstant.INVALID_USER_NAME)
    @Schema(description = "用户名", requiredMode = REQUIRED, pattern = RegexpConstant.USER_NAME, example = "guest")
    private String name;

    @NotBlank(message = MessageConstant.FIELD_NOT_BLANK)
    @Pattern(regexp = RegexpConstant.ACCOUNT, message = MessageConstant.INVALID_ACCOUNT)
    @Schema(description = "账号", requiredMode = REQUIRED, pattern = RegexpConstant.ACCOUNT, example = "guest")
    private String account;

    @NotBlank(message = MessageConstant.FIELD_NOT_BLANK)
    @Pattern(regexp = RegexpConstant.PASSWORD, message = MessageConstant.INVALID_PASSWORD)
    @Schema(description = "密码", requiredMode = REQUIRED, pattern = RegexpConstant.PASSWORD, example = "guest")
    private String password;

    @Pattern(regexp = RegexpConstant.GENDER, message = MessageConstant.INVALID_GENDER)
    @Schema(description = "性别", pattern = RegexpConstant.GENDER, example = GenderConstant.MALE)
    private String gender;

    @Pattern(regexp = RegexpConstant.PHONE, message = MessageConstant.INVALID_PHONE)
    @Schema(description = "电话号码", example = "12345678901")
    private String phone;

    @NotBlank(message = MessageConstant.FIELD_NOT_BLANK)
    @Email(regexp = RegexpConstant.EMAIL, message = MessageConstant.INVALID_EMAIL)
    @Schema(description = "邮箱", pattern = RegexpConstant.EMAIL, example = "example@qq.com")
    private String email;

    @Schema(description = "联系地址", example = "广东省广州市海珠区")
    private String address;

}
