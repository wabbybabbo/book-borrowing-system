package org.example.admin.pojo.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import org.example.common.constant.MessageConstant;

import static io.swagger.v3.oas.annotations.media.Schema.RequiredMode.REQUIRED;

/**
 * 发送提醒消息给用户时传递的数据模型
 */
@Data
@Schema(description = "发送提醒消息给用户时传递的数据模型")
public class SendReminderDTO {

    @NotNull(message = MessageConstant.FIELD_NOT_NULL)
    @Schema(description = "借阅记录ID", requiredMode = REQUIRED)
    private String id;

    @NotNull(message = MessageConstant.FIELD_NOT_NULL)
    @Schema(description = "用户ID", requiredMode = REQUIRED)
    private String userId;

}
