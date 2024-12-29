package org.example.admin.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.admin.pojo.query.PageQuery;
import org.example.admin.mapper.BorrowStatisticMapper;
import org.example.admin.entity.BorrowStatistic;
import org.example.admin.service.IBorrowStatisticService;
import org.example.common.constant.MessageConstant;
import org.example.common.exception.NotFoundException;
import org.example.common.result.PageResult;
import org.springframework.jdbc.BadSqlGrammarException;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 借阅数量统计表 服务实现类
 * </p>
 *
 * @author zhengjunpeng
 * @since 2024-05-05
 */
@Slf4j
@RequiredArgsConstructor
@Service
public class BorrowStatisticServiceImpl extends ServiceImpl<BorrowStatisticMapper, BorrowStatistic> implements IBorrowStatisticService {

    private final BorrowStatisticMapper borrowStatisticMapper;

    @Override
    public PageResult<BorrowStatistic> pageQuery(PageQuery pageQuery) {
        // 构建分页查询条件
        Page<BorrowStatistic> page = pageQuery.toMpPage();
        QueryWrapper<BorrowStatistic> queryWrapper = new QueryWrapper<>();

        // 分页查询
        try {
            borrowStatisticMapper.selectPage(page, queryWrapper);
        } catch (BadSqlGrammarException e) {
            log.error("[log] 分页查询借阅数量统计数据失败 BadSqlGrammarException: {}, msg: {}", e.getMessage(), MessageConstant.FIELD_NOT_FOUND);
            throw new NotFoundException(MessageConstant.FIELD_NOT_FOUND);
        }

        return PageResult.<BorrowStatistic>builder()
                .total(page.getTotal())
                .pages(page.getPages())
                .records(page.getRecords())
                .build();
    }

}
