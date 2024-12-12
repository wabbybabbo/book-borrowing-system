package org.example.admin.pojo.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import org.example.common.constant.MessageConstant;

@Data
@Schema(description = "更改图书信息时传递的数据模型", requiredProperties = {"id"})
public class UpdateBookDTO {

    @Schema(description = "图书ID")
    @NotNull(message = MessageConstant.FIELD_NOT_NULL)
    private Integer id;

    @Schema(description = "图书类别ID")
    private String categoryId;

    @Schema(description = "书名")
    private String name;

    @Schema(description = "国际标准书号")
    private String isbn;

    @Schema(description = "作者")
    private String author;

    @Schema(description = "出版社")
    private String publisher;

    @Schema(description = "描述")
    private String description;

    @Schema(description = "库存数量")
    private Integer stock;

}
