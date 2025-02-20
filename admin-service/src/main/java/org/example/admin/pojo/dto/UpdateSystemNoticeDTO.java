package org.example.admin.pojo.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.Data;
import org.example.common.constant.MessageConstant;
import org.example.common.constant.RegexpConstant;

import static io.swagger.v3.oas.annotations.media.Schema.RequiredMode.REQUIRED;

/**
 * 更改系统通知消息时传递的数据模型
 */
@Data
@Schema(description = "更改系统通知消息时传递的数据模型")
public class UpdateSystemNoticeDTO {

    @NotNull(message = MessageConstant.FIELD_NOT_NULL)
    @Schema(description = "系统通知消息ID", requiredMode = REQUIRED)
    private String id;

    @Pattern(regexp = RegexpConstant.NOTICE_TITLE, message = MessageConstant.INVALID_NOTICE_TITLE)
    @Schema(description = "系统通知消息标题", pattern = RegexpConstant.PUBLISHER_NAME, example = "系统维护")
    private String title;

    @Schema(description = "系统通知消息内容")
    private String content;

}
