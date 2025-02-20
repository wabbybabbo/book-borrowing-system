package org.example.admin.pojo.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Data;
import org.example.common.constant.MessageConstant;
import org.example.common.constant.RegexpConstant;

import static io.swagger.v3.oas.annotations.media.Schema.RequiredMode.REQUIRED;

/**
 * 新建系统通知消息时传递的数据模型
 */
@Data
@Schema(description = "新建系统通知消息时传递的数据模型")
public class CreateSystemNoticeDTO {

    @NotBlank(message = MessageConstant.FIELD_NOT_BLANK)
    @Pattern(regexp = RegexpConstant.NOTICE_TITLE, message = MessageConstant.INVALID_NOTICE_TITLE)
    @Schema(description = "系统通知消息标题", requiredMode = REQUIRED, pattern = RegexpConstant.PUBLISHER_NAME, example = "系统维护")
    private String title;

    @NotBlank(message = MessageConstant.FIELD_NOT_BLANK)
    @Schema(description = "系统通知消息内容", requiredMode = REQUIRED, pattern = RegexpConstant.PUBLISHER_NAME, example = "系统将于2025年1月1日至2025年1月2日进行维护。")
    private String content;

}
