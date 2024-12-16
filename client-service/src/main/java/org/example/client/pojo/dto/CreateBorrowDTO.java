package org.example.client.pojo.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.Pattern;
import lombok.Data;
import org.example.common.constant.MessageConstant;
import org.example.common.constant.RegexpConstant;

import java.time.LocalDate;

import static io.swagger.v3.oas.annotations.media.Schema.RequiredMode.REQUIRED;

@Data
@Schema(description = "新增书籍借阅记录时传递的数据模型")
public class CreateBorrowDTO {

    @Schema(description = "国际标准书号", pattern = RegexpConstant.ISBN, requiredMode = REQUIRED, example = "9787115583864")
    @Pattern(regexp = RegexpConstant.ISBN, message = MessageConstant.INVALID_ISBN)
    private String isbn;

    @Schema(description = "借阅预约日期", requiredMode = REQUIRED, example = "1970-01-01")
    @Future(message = MessageConstant.INVALID_RESERVE_DATE)
    private LocalDate reserveDate;

    @Schema(description = "预计归还日期", requiredMode = REQUIRED, example = "1970-01-02")
    @Future(message = MessageConstant.INVALID_RETURN_DATE)
    private LocalDate returnDate;

}
