package org.example.client.service;

import com.baomidou.mybatisplus.extension.service.IService;
import org.example.client.entity.Category;
import org.example.client.pojo.vo.CategoryVO;

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
     * 查询所有书籍类别
     *
     * @return {@link List<CategoryVO>}
     */
    List<CategoryVO> getCategories();

}
