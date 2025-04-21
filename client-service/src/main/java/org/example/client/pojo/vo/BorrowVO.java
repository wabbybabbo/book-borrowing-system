package org.example.client.pojo.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * 用户查询借阅记录时响应的数据模型
 */
@Data
@Schema(description = "用户查询借阅记录时响应的数据模型")
public class BorrowVO implements Serializable {

    @Schema(description = "借阅记录ID")
    private String id;

    @Schema(description = "借阅状态")
    private String status;

    @Schema(description = "书籍名称")
    private String bookName;

    @Schema(description = "国际标准书号")
    private String isbn;

    @Schema(description = "借阅预约日期")
    private LocalDate reserveDate;

    @Schema(description = "预计归还日期")
    private LocalDate returnDate;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Schema(description = "实际归还时间")
    private LocalDateTime updateTime;

}
