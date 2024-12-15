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
 * 收藏记录表
 * </p>
 *
 * @author zhengjunpeng
 * @since 2024-04-07
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("favorite")
@Schema(description = "收藏记录表")
public class Favorite implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    @Schema(description = "收藏记录ID")
    @TableId(value = "id", type = IdType.ASSIGN_ID)
    private Long id;

    @Schema(description = "用户ID")
    private Long userId;

    @Schema(description = "书籍ID")
    private Long bookId;

    @Schema(description = "创建时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:MM:SS")
    private LocalDateTime createTime;

    @Schema(description = "更新时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:MM:SS")
    private LocalDateTime updateTime;


}
