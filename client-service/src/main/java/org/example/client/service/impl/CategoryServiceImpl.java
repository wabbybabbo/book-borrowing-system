package org.example.client.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.client.mapper.CategoryMapper;
import org.example.client.pojo.entity.Category;
import org.example.client.pojo.vo.CategoryVO;
import org.example.client.service.ICategoryService;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p>
 * 图书类别表 服务实现类
 * </p>
 *
 * @author wabbybabbo
 * @since 2024-04-07
 */
@Slf4j
@RequiredArgsConstructor
@Service
public class CategoryServiceImpl extends ServiceImpl<CategoryMapper, Category> implements ICategoryService {

    private final CategoryMapper categoryMapper;

    @Override
    public List<CategoryVO> getCategories() {
        // 查询所有图书类别名称
        return categoryMapper.getCategories();
    }
}
