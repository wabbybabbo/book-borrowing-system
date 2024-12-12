package org.example.client.pojo.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Future;
import lombok.Data;
import org.example.common.constant.MessageConstant;
import org.hibernate.validator.constraints.Length;

import java.time.LocalDate;

@Data
@Schema(description = "新增图书借阅记录时传递的数据模型", requiredProperties = {"isbn", "reserveDate", "returnDate"})
public class CreateBorrowDTO {

    @Schema(description = "国际标准书号")
    @Length(min = 13, max = 13, message = MessageConstant.INVALID_ISBN)
    private String isbn;

    @Schema(description = "借阅预约日期", example = "2024-01-01")
    @Future(message = MessageConstant.INVALID_RESERVE_DATE)
    private LocalDate reserveDate;

    @Schema(description = "预计归还日期", example = "2024-01-01")
    @Future(message = MessageConstant.INVALID_RETURN_DATE)
    private LocalDate returnDate;

}
