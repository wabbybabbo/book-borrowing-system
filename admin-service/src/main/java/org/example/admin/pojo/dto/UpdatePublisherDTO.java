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
 * 更改出版社信息时传递的数据模型
 */
@Data
@Schema(description = "更改出版社信息时传递的数据模型")
public class UpdatePublisherDTO {

    @NotNull(message = MessageConstant.FIELD_NOT_NULL)
    @Schema(description = "出版社ID", requiredMode = REQUIRED)
    private String id;

    @NotBlank(message = MessageConstant.FIELD_NOT_BLANK)
    @Pattern(regexp = RegexpConstant.PUBLISHER_NAME, message = MessageConstant.INVALID_PUBLISHER_NAME)
    @Schema(description = "出版社名称", requiredMode = REQUIRED, pattern = RegexpConstant.PUBLISHER_NAME, example = "计算机科学与技术")
    private String name;

}
