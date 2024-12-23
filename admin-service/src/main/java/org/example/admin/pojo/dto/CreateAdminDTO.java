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
@Schema(description = "新建管理员账号时传递的数据模型")
public class CreateAdminDTO {

    @Pattern(regexp = RegexpConstant.ADMIN_NAME, message = MessageConstant.INVALID_ADMIN_NAME)
    @Schema(description = "管理员名称", pattern = RegexpConstant.ADMIN_NAME, requiredMode = REQUIRED, example = "admin")
    private String name;

    @Pattern(regexp = RegexpConstant.ACCOUNT, message = MessageConstant.INVALID_ACCOUNT)
    @Schema(description = "账号", pattern = RegexpConstant.ACCOUNT, requiredMode = REQUIRED, example = "admin")
    private String account;

    @Pattern(regexp = RegexpConstant.PASSWORD, message = MessageConstant.INVALID_PASSWORD)
    @Schema(description = "密码", pattern = RegexpConstant.PASSWORD, requiredMode = REQUIRED, example = "admin")
    private String password;

    @Pattern(regexp = RegexpConstant.GENDER, message = MessageConstant.INVALID_GENDER)
    @Schema(description = "性别", pattern = RegexpConstant.GENDER, example = GenderConstant.MALE)
    private String gender;

    @Pattern(regexp = RegexpConstant.PHONE, message = MessageConstant.INVALID_PHONE)
    @Schema(description = "电话号码", pattern = RegexpConstant.PHONE, example = "19876543210")
    private String phone;

    @Email(regexp = RegexpConstant.EMAIL, message = MessageConstant.INVALID_EMAIL)
    @Schema(description = "邮箱", example = "example@qq.com")
    private String email;

    @Schema(description = "联系地址", example = "广东省广州市海珠区")
    private String address;

}
