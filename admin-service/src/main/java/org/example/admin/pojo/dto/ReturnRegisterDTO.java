package org.example.admin.pojo.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.Data;
import org.example.common.constant.MessageConstant;
import org.example.common.constant.RegexpConstant;

import static io.swagger.v3.oas.annotations.media.Schema.RequiredMode.REQUIRED;

@Data
@Schema(description = "用户归还借阅的书籍时传递的数据模型")
public class ReturnRegisterDTO {

    @Schema(description = "借阅记录ID", requiredMode = REQUIRED)
    @NotNull(message = MessageConstant.FIELD_NOT_NULL)
    private String id;

    @Schema(description = "国际标准书号", pattern = RegexpConstant.ISBN, requiredMode = REQUIRED, example = "9787115583864")
    @Pattern(regexp = RegexpConstant.ISBN, message = MessageConstant.INVALID_ISBN)
    private String isbn;

}
