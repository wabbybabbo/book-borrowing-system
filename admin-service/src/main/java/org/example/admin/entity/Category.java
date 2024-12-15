package org.example.admin.entity;

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
 * 书籍类别表
 * </p>
 *
 * @author zhengjunpeng
 * @since 2024-04-07
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("category")
@Schema(description = "书籍类别表")
public class Category implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    @Schema(description = "书籍类别ID")
    @TableId(value = "id", type = IdType.ASSIGN_ID)
    private Long id;

    @Schema(description = "类别名称")
    private String name;

    @Schema(description = "创建时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:MM:SS")
    private LocalDateTime createTime;

    @Schema(description = "更新时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:MM:SS")
    private LocalDateTime updateTime;


}
