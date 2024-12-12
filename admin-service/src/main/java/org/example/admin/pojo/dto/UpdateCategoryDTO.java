package org.example.admin.pojo.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import org.example.common.constant.MessageConstant;

@Data
@Schema(description = "更改图书类别信息时传递的数据模型", requiredProperties = {"id", "name"})
public class UpdateCategoryDTO {

    @Schema(description = "图书类别ID")
    @NotNull(message = MessageConstant.FIELD_NOT_NULL)
    private Integer id;

    @Schema(description = "类别名称")
    @NotBlank(message = MessageConstant.FIELD_NOT_BLANK)
    private String name;

}
