package org.example.client.pojo.query;

import com.baomidou.mybatisplus.core.metadata.OrderItem;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Data
@Schema(description = "分页查询时传递的数据模型")
public class PageQuery {

    @Schema(description = "当前页号", defaultValue = "1")
    private Integer current = 1;

    @Schema(description = "每页显示记录数", defaultValue = "10")
    private Integer size = 10;

    @Schema(description = "筛选条件", example = "category_id=1,title~vue")
    private List<String> filterConditions = new ArrayList<>();

    @Schema(description = "排序字段")
    private String sortBy = "";

    @Schema(description = "是否升序", defaultValue = "true")
    private Boolean isAsc = true;

    public <T> Page<T> toMpPage(OrderItem... items) {
        // 设置分页条件
        Page<T> page = Page.of(current, size);
        // 设置排序条件
        if (StringUtils.isNotBlank(sortBy)) {
            page.addOrder(new OrderItem().setColumn(sortBy).setAsc(isAsc));
        } else if (Objects.nonNull(items)) {
            page.addOrder(items);
        }
        return page;
    }

}
