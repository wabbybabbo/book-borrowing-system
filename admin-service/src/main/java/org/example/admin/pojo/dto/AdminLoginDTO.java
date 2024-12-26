package org.example.admin.pojo.dto;


import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.Data;
import org.example.common.constant.MessageConstant;
import org.example.common.constant.RegexpConstant;

import static io.swagger.v3.oas.annotations.media.Schema.RequiredMode.REQUIRED;

@Data
@Schema(description = "管理员登录时传递的数据模型")
public class AdminLoginDTO {

    @NotBlank(message = MessageConstant.FIELD_NOT_BLANK)
    @Pattern(regexp = RegexpConstant.ACCOUNT, message = MessageConstant.INVALID_ACCOUNT)
    @Schema(description = "账号", requiredMode = REQUIRED, pattern = RegexpConstant.ACCOUNT, example = "admin")
    private String account;

    @NotBlank(message = MessageConstant.FIELD_NOT_BLANK)
    @Pattern(regexp = RegexpConstant.PASSWORD, message = MessageConstant.INVALID_PASSWORD)
    @Schema(description = "密码", requiredMode = REQUIRED, pattern = RegexpConstant.PASSWORD, example = "admin")
    private String password;

    @NotBlank(message = MessageConstant.FIELD_NOT_BLANK)
    @Schema(description = "验证码", requiredMode = REQUIRED, example = "1")
    private String code;

    @NotNull(message = MessageConstant.FIELD_NOT_NULL)
    @Schema(description = "获取验证码的时间戳", requiredMode = REQUIRED, example = "1234567890")
    private String timestamp;

}
