package org.example.admin.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Select;
import org.example.admin.entity.Category;
import org.example.admin.pojo.vo.CategoryVO;

import java.util.List;

/**
 * <p>
 * 书籍类别表 Mapper 接口
 * </p>
 *
 * @author zhengjunpeng
 * @since 2024-04-07
 */
public interface CategoryMapper extends BaseMapper<Category> {

    /**
     * 查询所有书籍类别
     *
     * @return 书籍类别列表
     */
    @Select("select id, name from category")
    List<CategoryVO> getCategories();

}
