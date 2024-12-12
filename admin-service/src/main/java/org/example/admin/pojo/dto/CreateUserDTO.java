package org.example.admin.pojo.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import org.example.common.constant.MessageConstant;

@Data
@Schema(description = "新建账号时传递的数据模型", requiredProperties = {"name", "password"})
public class CreateUserDTO {

    @Schema(description = "用户角色 默认为user")
    private String role;

    @Schema(description = "用户名")
    @NotBlank(message = MessageConstant.FIELD_NOT_BLANK)
    private String name;

    @Schema(description = "密码")
    @NotBlank(message = MessageConstant.FIELD_NOT_BLANK)
    private String password;

    @Schema(description = "性别")
    private String gender;

    @Schema(description = "电话号码")
    private String phone;

    @Schema(description = "邮箱")
    private String email;

    @Schema(description = "地址")
    private String address;

}
