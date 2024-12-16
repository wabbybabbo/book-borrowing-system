package org.example.admin.pojo.dto;


import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Pattern;
import lombok.Data;
import org.example.common.constant.MessageConstant;
import org.example.common.constant.RegexpConstant;

import static io.swagger.v3.oas.annotations.media.Schema.RequiredMode.REQUIRED;

@Data
@Schema(description = "管理员登录时传递的数据模型")
public class AdminLoginDTO {

    @Schema(description = "账号", pattern = RegexpConstant.ACCOUNT, requiredMode = REQUIRED, example = "admin")
    @Pattern(regexp = RegexpConstant.ACCOUNT, message = MessageConstant.INVALID_ACCOUNT)
    private String account;

    @Schema(description = "密码", pattern = RegexpConstant.PASSWORD, requiredMode = REQUIRED, example = "admin")
    @Pattern(regexp = RegexpConstant.PASSWORD, message = MessageConstant.INVALID_PASSWORD)
    private String password;

}
