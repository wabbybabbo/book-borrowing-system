package org.example.admin.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Select;
import org.example.admin.pojo.entity.Category;

/**
 * <p>
 * 图书类别表 Mapper 接口
 * </p>
 *
 * @author wabbybabbo
 * @since 2024-04-07
 */
public interface CategoryMapper extends BaseMapper<Category> {

    /**
     * 根据categoryName查询categoryId
     *
     * @param categoryName 图书类别名
     * @return categoryId
     */
    @Select("select id from category where name=#{categoryName}")
    Integer selectIdByName(String categoryName);

}
