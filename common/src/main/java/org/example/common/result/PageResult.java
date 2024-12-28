package org.example.common.result;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * 分页查询结果封装类
 * @param <T> 查询结果数据集合的元素
 */
@Data
@Builder
@Schema(description = "分页查询结果封装类")
public class PageResult<T> implements Serializable {

    @Schema(description = "总条数")
    private Long total;

    @Schema(description = "总页数")
    private Long pages;

    @Schema(description = "数据集合")
    private List<T> records;

}
