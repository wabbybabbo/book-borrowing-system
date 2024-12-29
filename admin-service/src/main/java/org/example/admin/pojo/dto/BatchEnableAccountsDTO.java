package org.example.admin.pojo.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotEmpty;
import lombok.Data;
import org.example.common.constant.MessageConstant;

import java.util.List;

import static io.swagger.v3.oas.annotations.media.Schema.RequiredMode.REQUIRED;

/**
 * 批量解禁账号时传递的数据模型
 */
@Data
@Schema(description = "批量解禁账号时传递的数据模型")
public class BatchEnableAccountsDTO {

    @NotEmpty(message = MessageConstant.FIELD_NOT_EMPTY)
    @Schema(description = "账号ID列表", requiredMode = REQUIRED, example = "1,2,3")
    private List<String> ids;

}
