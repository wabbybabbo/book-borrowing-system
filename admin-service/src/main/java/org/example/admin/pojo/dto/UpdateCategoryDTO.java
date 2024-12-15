package org.example.admin.pojo.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import org.example.common.constant.MessageConstant;

@Data
@Schema(description = "更改书籍类别信息时传递的数据模型", requiredProperties = {"id", "name"})
public class UpdateCategoryDTO {

    @Schema(description = "书籍类别ID")
    @NotNull(message = MessageConstant.FIELD_NOT_NULL)
    private Long id;

    @Schema(description = "类别名称")
    @NotBlank(message = MessageConstant.FIELD_NOT_BLANK)
    private String name;

}
