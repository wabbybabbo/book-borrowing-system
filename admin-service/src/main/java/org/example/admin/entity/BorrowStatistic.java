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

/**
 * <p>
 * 借阅数量统计表
 * </p>
 *
 * @author zhengjunpeng
 * @since 2024-05-05
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("borrow_statistic")
@Schema(description = "借阅数量统计表")
public class BorrowStatistic implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    @TableId(value = "date", type = IdType.NONE)
    @JsonFormat(pattern = "yyyy-MM-dd")//todo 是否必要
    @Schema(description = "日期")
    private LocalDate date;

    @Schema(description = "借阅数量")
    private Integer quantity;


}
