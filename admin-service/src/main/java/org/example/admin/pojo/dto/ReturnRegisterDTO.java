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

    @NotNull(message = MessageConstant.FIELD_NOT_NULL)
    @Schema(description = "借阅记录ID", requiredMode = REQUIRED)
    private String id;

    @NotNull(message = MessageConstant.FIELD_NOT_NULL)
    @Pattern(regexp = RegexpConstant.ISBN, message = MessageConstant.INVALID_ISBN)
    @Schema(description = "国际标准书号", requiredMode = REQUIRED, pattern = RegexpConstant.ISBN, example = "9787115583864")
    private String isbn;

}
