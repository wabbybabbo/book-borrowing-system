package org.example.admin.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.admin.mapper.CategoryMapper;
import org.example.admin.pojo.dto.CreateCategoryDTO;
import org.example.admin.pojo.dto.UpdateCategoryDTO;
import org.example.admin.pojo.entity.Category;
import org.example.admin.pojo.query.PageQuery;
import org.example.admin.service.ICategoryService;
import org.example.common.constant.MessageConstant;
import org.example.common.exception.AlreadyExistsException;
import org.example.common.exception.NotAllowedException;
import org.example.common.exception.NotFoundException;
import org.example.common.result.PageResult;
import org.springframework.beans.BeanUtils;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.jdbc.BadSqlGrammarException;
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
    public PageResult<Category> pageQuery(PageQuery pageQuery) {
        // 构建分页查询条件
        Page<Category> page = pageQuery.toMpPage();
        QueryWrapper<Category> queryWrapper = new QueryWrapper<Category>();
        List<String> filterConditions = pageQuery.getFilterConditions();
        log.info("[log] filterConditions: {}", filterConditions);
        if (null != filterConditions && !filterConditions.isEmpty()) {
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
            log.error("[log] BadSqlGrammarException: {}", e.getMessage());
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
        // 构建图书类别对象
        Category category = new Category();
        category.setName(createCategoryDTO.getName());
        // 新增图书类别
        try {
            categoryMapper.insert(category);
        } catch (DuplicateKeyException e) {
            log.error("[log] DuplicateKeyException: {}", e.getMessage());
            throw new AlreadyExistsException(MessageConstant.CATEGORY_ALREADY_EXISTS);
        }
    }

    @Override
    public List<Category> getCategories() {
        // 构建查询条件
        QueryWrapper<Category> queryWrapper = new QueryWrapper<>();
        // 查询所有图书类别名称
        return categoryMapper.selectList(queryWrapper);
    }

    @Override
    public void updateCategory(UpdateCategoryDTO updateCategoryDTO) {
        // 构建图书类别对象
        Category category = new Category();
        BeanUtils.copyProperties(updateCategoryDTO, category);
        // 更改图书类别信息
        try {
            categoryMapper.updateById(category);
        } catch (DuplicateKeyException e) {
            log.error("[log] DuplicateKeyException: {}", e.getMessage());
            throw new AlreadyExistsException(MessageConstant.CATEGORY_ALREADY_EXISTS);
        }
    }

    @Override
    public void deleteCategory(Integer id) {
        // 删除图书类别
        try {
            categoryMapper.deleteById(id);
        } catch (DataIntegrityViolationException e) {
            throw new NotAllowedException(MessageConstant.CATEGORY_BE_RELATED_BY_BOOK);
        }
    }

    @Override
    public void batchDeleteCategories(List<Integer> ids) {
        // 批量删除图书类别
        try {
            categoryMapper.deleteBatchIds(ids);
        } catch (DataIntegrityViolationException e) {
            throw new NotAllowedException(MessageConstant.CATEGORY_BE_RELATED_BY_BOOK);
        }
    }
}
