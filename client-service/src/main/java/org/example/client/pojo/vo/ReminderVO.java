package org.example.client.pojo.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * 查询用户提醒消息时响应的数据模型
 */
@Data
@Schema(description = "查询用户提醒消息时响应的数据模型")
public class ReminderVO {

    @Schema(description = "提醒消息记录ID")
    private String id;

    @Schema(description = "标题")
    private String title;

    @Schema(description = "提醒消息内容")
    private String content;

    @Schema(description = "是否已读：0-未读（默认），1-已读")
    private Boolean isRead;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Schema(description = "消息接收时间")
    private LocalDateTime createTime;

}
