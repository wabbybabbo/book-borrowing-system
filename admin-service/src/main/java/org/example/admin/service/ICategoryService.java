package org.example.admin.service;

import com.baomidou.mybatisplus.extension.service.IService;
import org.example.admin.pojo.dto.CreateCategoryDTO;
import org.example.admin.pojo.dto.UpdateCategoryDTO;
import org.example.admin.entity.Category;
import org.example.admin.pojo.query.PageQuery;
import org.example.common.result.PageResult;

import java.util.List;

/**
 * <p>
 * 书籍类别表 服务类
 * </p>
 *
 * @author zhengjunpeng
 * @since 2024-04-07
 */
public interface ICategoryService extends IService<Category> {
    /**
     * 新建书籍类别
     *
     * @param createCategoryDTO 新建书籍类别时传递的数据模型
     */
    void createCategory(CreateCategoryDTO createCategoryDTO);

    /**
     * 查询所有书籍类别
     *
     * @return 书籍类别列表
     */
    List<Category> getCategories();

    /**
     * 更改书籍类别信息
     *
     * @param updateCategoryDTO 更改书籍类别信息时传递的数据模型
     */
    void updateCategory(UpdateCategoryDTO updateCategoryDTO);

    /**
     * 删除书籍类别
     *
     * @param id 书籍类别ID
     */
    void deleteCategory(Long id);

    /**
     * 批量删除书籍类别
     *
     * @param ids 书籍类别ID列表
     */
    void batchDeleteCategories(List<Long> ids);

    /**
     * 分页查询书籍类别
     *
     * @param pageQuery 分页查询时传递的数据模型
     * @return 书籍类别列表
     */
    PageResult<Category> pageQuery(PageQuery pageQuery);

}
