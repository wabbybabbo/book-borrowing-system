package org.example.client.pojo.dto;


import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Data;
import org.example.common.constant.MessageConstant;
import org.example.common.constant.RegexpConstant;

import static io.swagger.v3.oas.annotations.media.Schema.RequiredMode.REQUIRED;

@Data
@Schema(description = "用户登录时传递的数据模型")
public class UserLoginDTO {

    @Schema(description = "账号", pattern = RegexpConstant.ACCOUNT, requiredMode = REQUIRED, example = "guest")
    @Pattern(regexp = RegexpConstant.ACCOUNT, message = MessageConstant.INVALID_ACCOUNT)
    private String account;

    @Schema(description = "密码", pattern = RegexpConstant.PASSWORD, requiredMode = REQUIRED, example = "guest")
    @Pattern(regexp = RegexpConstant.PASSWORD, message = MessageConstant.INVALID_PASSWORD)
    private String password;

    @NotBlank(message = MessageConstant.FIELD_NOT_BLANK)
    @Schema(description = "验证码", requiredMode = REQUIRED, example = "1")
    private String code;

    @NotBlank(message = MessageConstant.FIELD_NOT_BLANK)
    @Schema(description = "获取验证码的时间戳", requiredMode = REQUIRED, example = "1234567890")
    private String timestamp;

}
