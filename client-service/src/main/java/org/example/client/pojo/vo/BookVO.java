package org.example.client.pojo.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serializable;

@Data
@Schema(description = "查询图书时响应的数据模型")
public class BookVO implements Serializable {

    @Schema(description = "图书ID")
    private Integer id;

    @Schema(description = "图书类别")
    private String categoryName;

    @Schema(description = "书名")
    private String name;

    @Schema(description = "图书封面图片文件路径")
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

}
