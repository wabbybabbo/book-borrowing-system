package org.example.client.pojo.dto;


import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import org.example.common.constant.MessageConstant;
import org.hibernate.validator.constraints.Length;

@Data
@Schema(description = "用户登录时传递的数据模型", requiredProperties = {"account", "password"})
public class UserLoginDTO {

    @Schema(description = "账号 4-16位")
    @Length(min = 4, max = 16, message = MessageConstant.INVALID_ACCOUNT)
    private String account;

    @Schema(description = "密码 4-16位")
    @Length(min = 4, max = 16, message = MessageConstant.INVALID_PASSWORD)
    private String password;

}
