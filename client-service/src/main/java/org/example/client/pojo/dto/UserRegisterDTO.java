package org.example.client.pojo.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import org.example.common.constant.MessageConstant;
import org.hibernate.validator.constraints.Length;

@Data
@Schema(description = "用户注册时传递的数据模型", requiredProperties = {"name", "password"})
public class UserRegisterDTO {

    @Schema(description = "用户名")
    @Length(min = 4, max = 16, message = MessageConstant.INVALID_USERNAME)
    private String name;

    @Schema(description = "密码")
    @Length(min = 4, max = 16, message = MessageConstant.INVALID_PASSWORD)
    private String password;

    @Schema(description = "性别 可选值(男, 女)")
    private String gender;

    @Schema(description = "电话号码")
    private String phone;

    @Schema(description = "邮箱")
    private String email;

    @Schema(description = "地址")
    private String address;

}
