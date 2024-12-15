package org.example.client.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * <p>
 * 书籍信息表
 * </p>
 *
 * @author zhengjunpeng
 * @since 2024-04-07
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("book")
@Schema(description = "书籍信息表")
public class Book implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    @Schema(description = "书籍ID")
    @TableId(value = "id", type = IdType.ASSIGN_ID)
    private Long id;

    @Schema(description = "书籍类别ID")
    private Long categoryId;

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

    @Schema(description = "创建时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:MM:SS")
    private LocalDateTime createTime;

    @Schema(description = "更新时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:MM:SS")
    private LocalDateTime updateTime;


}
