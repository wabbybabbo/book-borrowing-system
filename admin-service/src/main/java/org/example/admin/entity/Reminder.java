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
 * 用户提醒消息表
 * </p>
 *
 * @author zhengjunpeng
 * @since 2025-02-13
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("reminder")
@Schema(description="用户提醒消息表")
public class Reminder implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    @Schema(description = "提醒消息记录ID")
    @TableId(value = "id", type = IdType.ASSIGN_ID)
    private String id;

    @Schema(description = "用户ID")
    private String userId;

    @Schema(description = "标题")
    private String title;

    @Schema(description = "提醒消息内容")
    private String content;

    @Schema(description = "是否已读：0-未读（默认），1-已读")
    private Boolean isRead;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Schema(description = "创建时间")
    private LocalDateTime createTime;


}
