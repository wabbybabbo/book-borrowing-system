package org.example.admin.pojo.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Pattern;
import lombok.Data;
import org.example.common.constant.MessageConstant;

@Data
@Schema(description = "更改管理员信息时传递的数据模型")
public class UpdateAdminDTO {

    @Schema(description = "管理员名称", pattern = "", example = "admin")
    @Pattern(regexp = "^\\S{2,16}$", message = MessageConstant.INVALID_ADMIN_NAME)
    private String name;

    @Schema(description = "账号", example = "admin")
    @Pattern(regexp = "^\\w{4,16}$", message = MessageConstant.INVALID_ACCOUNT)
    private String account;

    @Schema(description = "密码", example = "admin")
    @Pattern(regexp = "^\\w{4,16}$", message = MessageConstant.INVALID_PASSWORD)
    private String password;

    @Schema(description = "性别", example = "男")
    @Pattern(regexp = "^[男女]$", message = MessageConstant.INVALID_GENDER)
    private String gender;

    @Schema(description = "电话号码", example = "12345678901")
    @Pattern(regexp = "^[1-9][0-9]{10}$", message = MessageConstant.INVALID_PHONE)
    private String phone;

    @Schema(description = "邮箱", example = "example@qq.com")
    @Email(message = MessageConstant.INVALID_EMAIL)
    private String email;

    @Schema(description = "联系地址", example = "广东省广州市海珠区")
    private String address;

}
