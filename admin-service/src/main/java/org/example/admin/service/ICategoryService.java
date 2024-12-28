package org.example.admin.service;

import com.baomidou.mybatisplus.extension.service.IService;
import org.example.admin.entity.Category;
import org.example.admin.pojo.dto.CreateCategoryDTO;
import org.example.admin.pojo.dto.UpdateCategoryDTO;
import org.example.admin.pojo.query.PageQuery;
import org.example.admin.pojo.vo.CategoryVO;
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
     * @param createCategoryDTO {@link CreateCategoryDTO}
     */
    void createCategory(CreateCategoryDTO createCategoryDTO);

    /**
     * 查询所有书籍类别
     *
     * @return {@link List<CategoryVO>}
     */
    List<CategoryVO> getCategories();

    /**
     * 更改书籍类别信息
     *
     * @param updateCategoryDTO {@link UpdateCategoryDTO}
     */
    void updateCategory(UpdateCategoryDTO updateCategoryDTO);

    /**
     * 删除书籍类别
     *
     * @param id 书籍类别ID
     */
    void deleteCategory(String id);

    /**
     * 批量删除书籍类别
     *
     * @param ids 书籍类别ID列表
     */
    void deleteBatchCategories(List<String> ids);

    /**
     * 分页查询书籍类别
     *
     * @param pageQuery {@link PageQuery}
     * @return {@link PageResult<Category>}
     */
    PageResult<Category> pageQuery(PageQuery pageQuery);

}
