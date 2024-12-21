package org.example.admin.pojo.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotEmpty;
import lombok.Data;
import org.example.common.constant.MessageConstant;

import java.util.List;

import static io.swagger.v3.oas.annotations.media.Schema.RequiredMode.REQUIRED;

@Data
@Schema(description = "批量禁用管理员账号时传递的数据模型")
public class BatchEnableAccountDTO {

    @NotEmpty(message = MessageConstant.FIELD_NOT_EMPTY)
    @Schema(description = "管理员ID列表", requiredMode = REQUIRED, type = "long[]", example = "1,2,3")
    private List<Long> ids;

}
