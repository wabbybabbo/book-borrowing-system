package org.example.admin.pojo.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.Data;
import org.example.common.constant.MessageConstant;
import org.example.common.constant.RegexpConstant;

import static io.swagger.v3.oas.annotations.media.Schema.RequiredMode.REQUIRED;

/**
 * 更改书籍类别信息时传递的数据模型
 */
@Data
@Schema(description = "更改书籍类别信息时传递的数据模型")
public class UpdateCategoryDTO {

    @NotNull(message = MessageConstant.FIELD_NOT_NULL)
    @Schema(description = "书籍类别ID", requiredMode = REQUIRED)
    private String id;

    @NotBlank(message = MessageConstant.FIELD_NOT_BLANK)
    @Pattern(regexp = RegexpConstant.CATEGORY_NAME, message = MessageConstant.INVALID_CATEGORY_NAME)
    @Schema(description = "类别名称", requiredMode = REQUIRED, pattern = RegexpConstant.CATEGORY_NAME, example = "计算机科学与技术")
    private String name;

}
