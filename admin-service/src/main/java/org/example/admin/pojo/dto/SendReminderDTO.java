package org.example.admin.pojo.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import org.example.common.constant.MessageConstant;

import java.time.LocalDate;

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

    @NotNull(message = MessageConstant.FIELD_NOT_NULL)
    @Schema(description = "借阅状态", requiredMode = REQUIRED)
    private String status;

    @NotNull(message = MessageConstant.FIELD_NOT_NULL)
    @Schema(description = "借阅书籍名称", requiredMode = REQUIRED)
    private String bookName;

    @NotNull(message = MessageConstant.FIELD_NOT_NULL)
    @Schema(description = "国际标准书号", requiredMode = REQUIRED)
    private String isbn;

    @NotNull(message = MessageConstant.FIELD_NOT_NULL)
    @Schema(description = "借阅预约日期", requiredMode = REQUIRED)
    private LocalDate reserveDate;

    @NotNull(message = MessageConstant.FIELD_NOT_NULL)
    @Schema(description = "预计归还日期", requiredMode = REQUIRED)
    private LocalDate returnDate;


}
