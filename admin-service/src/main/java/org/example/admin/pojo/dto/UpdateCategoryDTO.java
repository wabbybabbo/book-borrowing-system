package org.example.admin.pojo.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.Data;
import org.example.common.constant.MessageConstant;
import org.example.common.constant.RegexpConstant;

import static io.swagger.v3.oas.annotations.media.Schema.RequiredMode.REQUIRED;

@Data
@Schema(description = "更改书籍类别信息时传递的数据模型")
public class UpdateCategoryDTO {

    @Schema(description = "书籍类别ID", requiredMode = REQUIRED)
    @NotNull(message = MessageConstant.FIELD_NOT_NULL)
    private String id;

    @Schema(description = "类别名称", pattern = RegexpConstant.CATEGORY_NAME, requiredMode = REQUIRED, example = "计算机科学与技术")
    @Pattern(regexp = RegexpConstant.CATEGORY_NAME, message = MessageConstant.INVALID_CATEGORY_NAME)
    private String name;

}
