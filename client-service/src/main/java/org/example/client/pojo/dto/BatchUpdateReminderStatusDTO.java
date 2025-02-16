package org.example.client.pojo.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotEmpty;
import lombok.Data;
import org.example.common.constant.MessageConstant;

import java.util.List;

import static io.swagger.v3.oas.annotations.media.Schema.RequiredMode.REQUIRED;

/**
 * 批量将用户的提醒消息标为已读或者未读时传递的数据模型
 */
@Data
@Schema(description = "批量将用户的提醒消息标为已读或者未读时传递的数据模型")
public class BatchUpdateReminderStatusDTO {

    @NotEmpty(message = MessageConstant.FIELD_NOT_EMPTY)
    @Schema(description = "提醒消息ID列表", requiredMode = REQUIRED, example = "1,2,3")
    private List<String> ids;

}
