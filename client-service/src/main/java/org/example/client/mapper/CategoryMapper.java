package org.example.client.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Select;
import org.example.client.pojo.entity.Category;
import org.example.client.pojo.vo.CategoryVO;

import java.util.List;

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
     * 查询所有图书类别项目
     *
     * @return 图书类别项目列表
     */
    @Select("select id, name from category")
    List<CategoryVO> getCategories();

}
