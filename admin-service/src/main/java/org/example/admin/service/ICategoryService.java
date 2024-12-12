package org.example.admin.service;

import com.baomidou.mybatisplus.extension.service.IService;
import org.example.admin.pojo.dto.CreateCategoryDTO;
import org.example.admin.pojo.dto.UpdateCategoryDTO;
import org.example.admin.pojo.entity.Category;
import org.example.admin.pojo.query.PageQuery;
import org.example.common.result.PageResult;

import java.util.List;

/**
 * <p>
 * 图书类别表 服务类
 * </p>
 *
 * @author wabbybabbo
 * @since 2024-04-07
 */
public interface ICategoryService extends IService<Category> {
    /**
     * 新建图书类别
     *
     * @param createCategoryDTO 新建图书类别时传递的数据模型
     */
    void createCategory(CreateCategoryDTO createCategoryDTO);

    /**
     * 查询所有图书类别项目
     *
     * @return 图书类别项目列表
     */
    List<Category> getCategories();

    /**
     * 更改图书类别项目信息
     *
     * @param updateCategoryDTO 更改图书类别项目信息时传递的数据模型
     */
    void updateCategory(UpdateCategoryDTO updateCategoryDTO);

    /**
     * 删除图书类别项目
     *
     * @param id 图书类别ID
     */
    void deleteCategory(Integer id);

    /**
     * 批量删除图书类别
     *
     * @param ids 图书类别ID列表
     */
    void batchDeleteCategories(List<Integer> ids);

    /**
     * 分页查询图书类别
     *
     * @param pageQuery 分页查询时传递的数据模型
     * @return 图书类别列表
     */
    PageResult<Category> pageQuery(PageQuery pageQuery);
}
