package org.example.client.pojo.query;

import com.baomidou.mybatisplus.core.metadata.OrderItem;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

/**
 * 分页查询时传递的数据模型
 */
@Data
@Schema(description = "分页查询时传递的数据模型")
public class PageQuery {

    @Schema(description = "当前页号,默认值(1)")
    private Long current = 1L;

    @Schema(description = "每页显示记录数,默认值(10)")
    private Long size = 10L;

    @Schema(description = "筛选条件,默认值([])", example = "category_id=1,name~vue")
    private List<String> filterConditions = new ArrayList<>();

    @Schema(description = "排序字段,默认值(\"\")", example = "create_time")
    private String sortBy = "";

    @Schema(description = "是否升序,默认值(true)")
    private Boolean isAsc = true;

    public <T> Page<T> toMpPage(OrderItem... items) {
        // 设置分页条件
        Page<T> page = Page.of(current, size);
        // 设置排序条件
        if (StringUtils.isNotBlank(sortBy)) {
            page.addOrder(new OrderItem().setColumn(sortBy).setAsc(isAsc));
        } else if (items != null) {
            page.addOrder(items);
        }
        return page;
    }

}
