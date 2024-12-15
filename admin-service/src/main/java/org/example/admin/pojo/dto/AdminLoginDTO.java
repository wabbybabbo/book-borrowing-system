package org.example.admin.pojo.dto;


import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import org.example.common.constant.MessageConstant;
import org.hibernate.validator.constraints.Length;

@Data
@Schema(description = "管理员登录时传递的数据模型")
public class AdminLoginDTO {

    @Schema(description = "账号", requiredMode = Schema.RequiredMode.REQUIRED, example = "admin")
    @Length(min = 4, max = 16, message = MessageConstant.INVALID_ACCOUNT)
    private String account;

    @Schema(description = "密码", requiredMode = Schema.RequiredMode.REQUIRED, example = "admin")
    @Length(min = 4, max = 16, message = MessageConstant.INVALID_PASSWORD)
    private String password;

}
