package org.example.admin.pojo.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

@Data
@Schema(description = "查询书籍信息时响应的数据模型")
public class BookVO implements Serializable {

    @Schema(description = "书籍ID")
    private String id;

    @Schema(description = "书籍类别ID")
    private String categoryId;

    @Schema(description = "书籍类名")
    private String categoryName;

    @Schema(description = "书籍名称")
    private String name;

    @Schema(description = "书籍封面图片文件路径")
    private String imgUrl;

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

    @JsonFormat(pattern = "yyyy-MM-dd HH:MM:SS")
    @Schema(description = "创建时间")
    private LocalDateTime createTime;

    @JsonFormat(pattern = "yyyy-MM-dd HH:MM:SS")
    @Schema(description = "更新时间")
    private LocalDateTime updateTime;

}
