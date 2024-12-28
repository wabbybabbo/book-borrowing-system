package org.example.admin.pojo.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.Data;
import org.example.common.constant.MessageConstant;
import org.example.common.constant.RegexpConstant;

import static io.swagger.v3.oas.annotations.media.Schema.RequiredMode.REQUIRED;

/**
 * 更改书籍信息时传递的数据模型
 */
@Data
@Schema(description = "更改书籍信息时传递的数据模型")
public class UpdateBookDTO {

    @NotNull(message = MessageConstant.FIELD_NOT_NULL)
    @Schema(description = "书籍ID", requiredMode = REQUIRED)
    private String id;

    @NotNull(message = MessageConstant.FIELD_NOT_NULL)
    @Schema(description = "书籍ID", requiredMode = REQUIRED)
    private String categoryId;

    @Pattern(regexp = RegexpConstant.BOOK_NAME, message = MessageConstant.INVALID_BOOK_NAME)
    @Schema(description = "书籍名称", pattern = RegexpConstant.BOOK_NAME, example = "Vue.js 设计与实现")
    private String name;

    @Pattern(regexp = RegexpConstant.ISBN, message = MessageConstant.INVALID_ISBN)
    @Schema(description = "国际标准书号", pattern = RegexpConstant.ISBN, example = "9787115583864")
    private String isbn;

    @Pattern(regexp = RegexpConstant.AUTHOR, message = MessageConstant.INVALID_AUTHOR_NAME)
    @Schema(description = "作者", pattern = RegexpConstant.AUTHOR, example = "霍春阳")
    private String author;

    @Pattern(regexp = RegexpConstant.PUBLISHER, message = MessageConstant.INVALID_PUBLISHER)
    @Schema(description = "出版社", pattern = RegexpConstant.PUBLISHER, example = "人民邮电出版社")
    private String publisher;

    @Schema(description = "描述", example = "基于 Vue.js 3\n深入解析 Vue.js 设计细节")
    private String description;

    @Schema(description = "库存数量", defaultValue = "0")
    private Integer stock;

}
