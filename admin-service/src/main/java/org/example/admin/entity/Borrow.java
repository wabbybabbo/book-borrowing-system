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
import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * <p>
 * 借阅记录表
 * </p>
 *
 * @author zhengjunpeng
 * @since 2024-04-07
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("borrow")
@Schema(description = "借阅记录表")
public class Borrow implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    @Schema(description = "借阅记录ID")
    @TableId(value = "id", type = IdType.ASSIGN_ID)
    private Long id;

    @Schema(description = "用户ID")
    private Long userId;

    @Schema(description = "借阅状态")
    private String status;

    @Schema(description = "借阅书籍名称")
    private String bookName;

    @Schema(description = "国际标准书号")
    private String isbn;

    @Schema(description = "借阅预约日期")
    private LocalDate reserveDate;

    @Schema(description = "预计归还日期")
    private LocalDate returnDate;

    @Schema(description = "创建时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:MM:SS")
    private LocalDateTime createTime;

    @Schema(description = "更新时间 实际归还时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:MM:SS")
    private LocalDateTime updateTime;

}
