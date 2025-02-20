package org.example.client.pojo.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Data;
import org.example.common.constant.MessageConstant;
import org.example.common.constant.RegexpConstant;

import static io.swagger.v3.oas.annotations.media.Schema.RequiredMode.REQUIRED;

/**
 * 用户注册时传递的数据模型
 */
@Data
@Schema(description = "用户注册时传递的数据模型")
public class UserRegisterDTO {

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

    @Schema(description = "性别：false-男，true-女", pattern = RegexpConstant.GENDER, example = "false")
    private Boolean gender;

    @Pattern(regexp = RegexpConstant.PHONE, message = MessageConstant.INVALID_PHONE)
    @Schema(description = "电话号码", example = "12345678901")
    private String phone;

    @NotBlank(message = MessageConstant.FIELD_NOT_BLANK)
    @Email(regexp = RegexpConstant.EMAIL, message = MessageConstant.INVALID_EMAIL)
    @Schema(description = "邮箱", requiredMode = REQUIRED, pattern = RegexpConstant.EMAIL, example = "example@qq.com")
    private String email;

    @Schema(description = "联系地址", example = "广东省广州市海珠区")
    private String address;

    @NotBlank(message = MessageConstant.FIELD_NOT_BLANK)
    @Schema(description = "验证码", requiredMode = REQUIRED, example = "AZaz09")
    private String code;

}
