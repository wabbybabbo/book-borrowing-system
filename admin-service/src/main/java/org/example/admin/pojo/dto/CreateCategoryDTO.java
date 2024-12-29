package org.example.admin.pojo.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Data;
import org.example.common.constant.MessageConstant;
import org.example.common.constant.RegexpConstant;

import static io.swagger.v3.oas.annotations.media.Schema.RequiredMode.REQUIRED;

/**
 * 新建书籍类别时传递的数据模型
 */
@Data
@Schema(description = "新建书籍类别时传递的数据模型")
public class CreateCategoryDTO {

    @NotBlank(message = MessageConstant.FIELD_NOT_BLANK)
    @Pattern(regexp = RegexpConstant.CATEGORY_NAME, message = MessageConstant.INVALID_CATEGORY_NAME)
    @Schema(description = "类别名称", requiredMode = REQUIRED, pattern = RegexpConstant.CATEGORY_NAME, example = "计算机科学与技术")
    private String name;

}
