package org.example.admin.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.admin.entity.Category;
import org.example.admin.mapper.CategoryMapper;
import org.example.admin.pojo.dto.CreateCategoryDTO;
import org.example.admin.pojo.dto.UpdateCategoryDTO;
import org.example.admin.pojo.query.PageQuery;
import org.example.admin.service.ICategoryService;
import org.example.common.constant.MessageConstant;
import org.example.common.exception.AlreadyExistsException;
import org.example.common.exception.NotAllowedException;
import org.example.common.exception.NotFoundException;
import org.example.common.result.PageResult;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.jdbc.BadSqlGrammarException;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p>
 * 书籍类别表 服务实现类
 * </p>
 *
 * @author zhengjunpeng
 * @since 2024-04-07
 */
@Slf4j
@RequiredArgsConstructor
@Service
public class CategoryServiceImpl extends ServiceImpl<CategoryMapper, Category> implements ICategoryService {

    private final CategoryMapper categoryMapper;

    @Override
    public PageResult<Category> pageQuery(PageQuery pageQuery) {
        // 构建分页查询条件
        Page<Category> page = pageQuery.toMpPage();
        QueryWrapper<Category> queryWrapper = new QueryWrapper<>();
        List<String> filterConditions = pageQuery.getFilterConditions();
        log.info("[log] 书籍类别分页查询条件 filterConditions: {}", filterConditions);
        if (CollUtil.isNotEmpty(filterConditions)) {
            for (String condition : filterConditions) {
                if (condition.contains("=")) {
                    log.info("[log] = condition: {}", condition);
                    String[] pair = condition.split("=");
                    if (pair.length == 2)
                        queryWrapper.eq(pair[0], pair[1]);
                } else if (condition.contains("~")) {
                    log.info("[log] ~ condition: {}", condition);
                    String[] pair = condition.split("~");
                    if (pair.length == 2)
                        queryWrapper.like(pair[0], pair[1]);
                }
            }
        }
        // 分页查询
        try {
            categoryMapper.selectPage(page, queryWrapper);
        } catch (BadSqlGrammarException e) {
            log.error("[log] 书籍类别分页查询失败 BadSqlGrammarException: {}, msg: {}", e.getMessage(), MessageConstant.FIELD_NOT_FOUND);
            throw new NotFoundException(MessageConstant.FIELD_NOT_FOUND);
        }

        return PageResult.<Category>builder()
                .total(page.getTotal())
                .pages(page.getPages())
                .records(page.getRecords())
                .build();
    }

    @Override
    public void createCategory(CreateCategoryDTO createCategoryDTO) {
        // 构建书籍类别对象
        Category category = new Category();
        category.setName(createCategoryDTO.getName());
        // 新增书籍类别
        try {
            categoryMapper.insert(category);
        } catch (DuplicateKeyException e) {
            log.error("[log] 新增书籍类别失败 DuplicateKeyException: {}, msg: {}", e.getMessage(), MessageConstant.CATEGORY_ALREADY_EXISTS);
            throw new AlreadyExistsException(MessageConstant.CATEGORY_ALREADY_EXISTS);
        }
    }

    @Override
    public List<Category> getCategories() {
        // 构建查询条件
        QueryWrapper<Category> queryWrapper = new QueryWrapper<>();
        // 查询所有书籍类别名称
        return categoryMapper.selectList(queryWrapper);
    }

    @Override
    public void updateCategory(UpdateCategoryDTO updateCategoryDTO) {
        // 构建书籍类别对象
        Category category = new Category();
        BeanUtil.copyProperties(updateCategoryDTO, category);
        // 更改书籍类别信息
        try {
            categoryMapper.updateById(category);
        } catch (DuplicateKeyException e) {
            log.error("[log] 更改书籍类别信息失败 DuplicateKeyException: {}, msg: {}", e.getMessage(), MessageConstant.CATEGORY_ALREADY_EXISTS);
            throw new AlreadyExistsException(MessageConstant.CATEGORY_ALREADY_EXISTS);
        }
    }

    @Override
    public void deleteCategory(Long id) {
        // 删除书籍类别
        try {
            categoryMapper.deleteById(id);
        } catch (DataIntegrityViolationException e) {
            log.error("[log] 删除书籍类别失败 DataIntegrityViolationException: {}, msg: {}", e.getMessage(), MessageConstant.CATEGORY_ALREADY_EXISTS);
            throw new NotAllowedException(MessageConstant.BOOK_REFERENCES_CATEGORY);
        }
    }

    @Override
    public void batchDeleteCategories(List<Long> ids) {
        // 批量删除书籍类别
        try {
            categoryMapper.deleteBatchIds(ids);
        } catch (DataIntegrityViolationException e) {
            log.error("[log] 批量删除书籍类别失败 DataIntegrityViolationException: {}, msg: {}", e.getMessage(), MessageConstant.CATEGORY_ALREADY_EXISTS);
            throw new NotAllowedException(MessageConstant.BOOK_REFERENCES_CATEGORY);
        }
    }

}
