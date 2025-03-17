package org.example.admin.pojo.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import org.example.common.constant.MessageConstant;
import org.example.common.constant.RegexpConstant;

import static io.swagger.v3.oas.annotations.media.Schema.RequiredMode.REQUIRED;

/**
 * 管理员换绑邮箱时传递的数据模型
 */
@Data
@Schema(description = "管理员换绑邮箱时传递的数据模型")
public class UpdateEmailDTO {

    @NotBlank(message = MessageConstant.FIELD_NOT_BLANK)
    @Email(regexp = RegexpConstant.EMAIL, message = MessageConstant.INVALID_EMAIL)
    @Schema(description = "邮箱", requiredMode = REQUIRED, pattern = RegexpConstant.EMAIL, example = "example@qq.com")
    private String email;

    @NotBlank(message = MessageConstant.FIELD_NOT_BLANK)
    @Schema(description = "验证码", requiredMode = REQUIRED, example = "AZaz09")
    private String code;

}
