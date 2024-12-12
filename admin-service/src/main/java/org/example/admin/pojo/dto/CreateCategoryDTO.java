package org.example.admin.pojo.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import org.example.common.constant.MessageConstant;

@Data
@Schema(description = "新增图书类别时传递的数据模型", requiredProperties = {"name"})
public class CreateCategoryDTO {

    @Schema(description = "类别名称")
    @NotBlank(message = MessageConstant.FIELD_NOT_BLANK)
    private String name;

}
