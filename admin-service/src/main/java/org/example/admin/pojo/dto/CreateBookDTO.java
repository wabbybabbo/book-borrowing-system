package org.example.admin.pojo.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.Data;
import org.example.common.constant.MessageConstant;
import org.example.common.constant.RegexpConstant;

import static io.swagger.v3.oas.annotations.media.Schema.RequiredMode.REQUIRED;

@Data
@Schema(description = "新增书籍时传递的数据模型")
public class CreateBookDTO {

    @NotNull(message = MessageConstant.FIELD_NOT_NULL)
    @Schema(description = "书籍类别ID", requiredMode = REQUIRED)
    private String categoryId;

    @NotBlank(message = MessageConstant.FIELD_NOT_BLANK)
    @Pattern(regexp = RegexpConstant.BOOK_NAME, message = MessageConstant.INVALID_BOOK_NAME)
    @Schema(description = "书籍名称", requiredMode = REQUIRED, pattern = RegexpConstant.BOOK_NAME, example = "Vue.js 设计与实现")
    private String name;

    @NotBlank(message = MessageConstant.FIELD_NOT_BLANK)
    @Pattern(regexp = RegexpConstant.ISBN, message = MessageConstant.INVALID_ISBN)
    @Schema(description = "国际标准书号", requiredMode = REQUIRED, pattern = RegexpConstant.ISBN, example = "9787115583864")
    private String isbn;

    @NotBlank(message = MessageConstant.FIELD_NOT_BLANK)
    @Pattern(regexp = RegexpConstant.AUTHOR, message = MessageConstant.INVALID_AUTHOR_NAME)
    @Schema(description = "作者", requiredMode = REQUIRED, pattern = RegexpConstant.AUTHOR, example = "霍春阳")
    private String author;

    @NotBlank(message = MessageConstant.FIELD_NOT_BLANK)
    @Pattern(regexp = RegexpConstant.PUBLISHER, message = MessageConstant.INVALID_PUBLISHER)
    @Schema(description = "出版社", requiredMode = REQUIRED, pattern = RegexpConstant.PUBLISHER, example = "人民邮电出版社")
    private String publisher;

    @Schema(description = "描述", example = "基于 Vue.js 3\n深入解析 Vue.js 设计细节")
    private String description;

    @Schema(description = "库存数量", defaultValue = "0")
    private Integer stock;

}
