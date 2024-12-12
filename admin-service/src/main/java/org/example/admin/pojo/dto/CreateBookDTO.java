package org.example.admin.pojo.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import org.example.common.constant.MessageConstant;
import org.hibernate.validator.constraints.Length;

@Data
@Schema(description = "新增图书时传递的数据模型", requiredProperties = {"img", "categoryId", "name", "isbn", "author", "publisher"})
public class CreateBookDTO {

    @Schema(description = "图书类别ID")
    @NotNull(message = MessageConstant.FIELD_NOT_NULL)
    private Integer categoryId;

    @Schema(description = "书名")
    @NotBlank(message = MessageConstant.FIELD_NOT_BLANK)
    private String name;

    @Schema(description = "国际标准书号")
    @Length(min = 13, max = 13, message = MessageConstant.INVALID_ISBN)
    private String isbn;

    @Schema(description = "作者")
    @NotBlank(message = MessageConstant.FIELD_NOT_BLANK)
    private String author;

    @Schema(description = "出版社")
    @NotBlank(message = MessageConstant.FIELD_NOT_BLANK)
    private String publisher;

    @Schema(description = "描述")
    private String description;

    @Schema(description = "库存数量 默认为0")
    private Integer stock;

}
