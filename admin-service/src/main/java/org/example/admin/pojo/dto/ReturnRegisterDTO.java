package org.example.admin.pojo.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import org.example.common.constant.MessageConstant;
import org.hibernate.validator.constraints.Length;

@Data
@Schema(description = "用户归还借阅的图书时传递的数据模型", requiredProperties = {"id", "isbn"})
public class ReturnRegisterDTO {

    @Schema(description = "借阅记录ID")
    @NotNull(message = MessageConstant.FIELD_NOT_NULL)
    private Integer id;

    @Schema(description = "国际标准书号")
    @Length(min = 13, max = 13, message = MessageConstant.INVALID_ISBN)
    private String isbn;

}
