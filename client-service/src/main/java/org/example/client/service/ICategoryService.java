package org.example.client.service;

import com.baomidou.mybatisplus.extension.service.IService;
import org.example.client.pojo.entity.Category;
import org.example.client.pojo.vo.CategoryVO;

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
     * 查询所有图书类别项目
     *
     * @return 图书类别项目列表
     */
    List<CategoryVO> getCategories();

}
