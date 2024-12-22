package org.example.admin.pojo.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Pattern;
import lombok.Data;
import org.example.common.constant.GenderConstant;
import org.example.common.constant.MessageConstant;
import org.example.common.constant.RegexpConstant;

/**
 * 更改管理员信息时传递的数据模型
 */
@Data
@Schema(description = "更改管理员信息时传递的数据模型")
public class UpdateAdminDTO {

    @Schema(description = "管理员名称", pattern = RegexpConstant.ADMIN_NAME, example = "admin")
    @Pattern(regexp = RegexpConstant.ADMIN_NAME, message = MessageConstant.INVALID_ADMIN_NAME)
    private String name;

    @Schema(description = "账号", pattern = RegexpConstant.ACCOUNT, example = "admin")
    @Pattern(regexp = RegexpConstant.ACCOUNT, message = MessageConstant.INVALID_ACCOUNT)
    private String account;

    @Schema(description = "密码", pattern = RegexpConstant.PASSWORD, example = "admin")
    @Pattern(regexp = RegexpConstant.PASSWORD, message = MessageConstant.INVALID_PASSWORD)
    private String password;

    @Schema(description = "性别", pattern = RegexpConstant.GENDER, example = GenderConstant.MALE)
    @Pattern(regexp = RegexpConstant.GENDER, message = MessageConstant.INVALID_GENDER)
    private String gender;

    @Schema(description = "电话号码", pattern = RegexpConstant.PHONE, example = "19876543210")
    @Pattern(regexp = RegexpConstant.PHONE, message = MessageConstant.INVALID_PHONE)
    private String phone;

    @Schema(description = "邮箱", example = "example@qq.com")
    @Email(regexp = RegexpConstant.EMAIL, message = MessageConstant.INVALID_EMAIL)
    private String email;

    @Schema(description = "联系地址", example = "广东省广州市海珠区")
    private String address;

}
