package org.example.admin.service.impl;

import cn.hutool.core.collection.CollUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.admin.entity.Book;
import org.example.admin.entity.Borrow;
import org.example.admin.mapper.BookMapper;
import org.example.admin.mapper.BorrowMapper;
import org.example.admin.pojo.dto.ReturnRegisterDTO;
import org.example.admin.pojo.query.PageQuery;
import org.example.admin.service.IBorrowService;
import org.example.common.constant.BorrowStatusConstant;
import org.example.common.constant.MessageConstant;
import org.example.common.constant.RabbitMQConstant;
import org.example.common.exception.NotFoundException;
import org.example.common.result.PageResult;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.jdbc.BadSqlGrammarException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

/**
 * <p>
 * 借阅记录表 服务实现类
 * </p>
 *
 * @author zhengjunpeng
 * @since 2024-04-07
 */
@Slf4j
@RequiredArgsConstructor
@Service
public class BorrowServiceImpl extends ServiceImpl<BorrowMapper, Borrow> implements IBorrowService {

    private final BorrowMapper borrowMapper;
    private final BookMapper bookMapper;
    private final RabbitTemplate rabbitTemplate;

    @Override
    public PageResult<Borrow> pageQuery(PageQuery pageQuery) {
        // 构建分页查询条件
        Page<Borrow> page = pageQuery.toMpPage();
        QueryWrapper<Borrow> queryWrapper = new QueryWrapper<>();
        List<String> filterConditions = pageQuery.getFilterConditions();
        log.info("[log] 借阅记录分页查询条件 filterConditions: {}", filterConditions);
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
            borrowMapper.selectPage(page, queryWrapper);
        } catch (BadSqlGrammarException e) {
            log.error("[log] 借阅记录分页查询失败 BadSqlGrammarException: {}, msg: {}", e.getMessage(), MessageConstant.FIELD_NOT_FOUND);
            throw new NotFoundException(MessageConstant.FIELD_NOT_FOUND);
        }

        return PageResult.<Borrow>builder()
                .total(page.getTotal())
                .pages(page.getPages())
                .records(page.getRecords())
                .build();
    }

    @Override
    public List<Borrow> getBorrows(String id) {
        LambdaQueryWrapper<Borrow> queryWrapper = new LambdaQueryWrapper<Borrow>()
                .eq(Borrow::getUserId, id);
        return borrowMapper.selectList(queryWrapper);
    }

    @Override
    @Transactional
    public void returnRegister(ReturnRegisterDTO returnRegisterDTO) {
        // 更改书籍库存 当前库存+1
        LambdaUpdateWrapper<Book> updateWrapper = new LambdaUpdateWrapper<Book>()
                .setSql("stock=stock+1")
                .eq(Book::getIsbn, returnRegisterDTO.getIsbn());
        bookMapper.update(updateWrapper);
        // 更改借阅状态为'已归还'
        Borrow borrow = new Borrow();
        borrow.setId(returnRegisterDTO.getId());
        borrow.setStatus(BorrowStatusConstant.RETURNED);
        borrowMapper.updateById(borrow);
    }

    @Override
    public void borrowRegister(String id) {
        // 构建借阅记录对象
        Borrow borrow = new Borrow();
        borrow.setId(id);
        borrow.setStatus(BorrowStatusConstant.BORROWING);
        // 更改借阅状态
        borrowMapper.updateById(borrow);
        // 发送消息，异步调用统计方法
        rabbitTemplate.convertAndSend(RabbitMQConstant.STATISTIC_DIRECT_EXCHANGE, RabbitMQConstant.STATISTIC_ROUTING_KEY, "");
    }

    @Override
    public void updateBorrowingBorrows() {
        log.info("[log] 将状态为'借阅中'并且超过预计归还日期的借阅记录的状态更新为'未按时归还'");
        // 查询当前日期超过预计归还日期的借阅记录
        LambdaQueryWrapper<Borrow> queryWrapper1 = new LambdaQueryWrapper<Borrow>()
                .select(Borrow::getId, Borrow::getUserId)
                .eq(Borrow::getStatus, BorrowStatusConstant.BORROWING)
                .lt(Borrow::getReturnDate, LocalDate.now());
        List<Borrow> borrowList = borrowMapper.selectList(queryWrapper1);

        if (borrowList.isEmpty()) return;

        // 将这些借阅记录的状态更改为'未按时归还'
        List<String> borrowIdList = borrowList.stream().map(Borrow::getId).collect(Collectors.toList());
        LambdaUpdateWrapper<Borrow> updateWrapper = new LambdaUpdateWrapper<Borrow>()
                .set(Borrow::getStatus, BorrowStatusConstant.RETURN_OVERDUE)
                .in(Borrow::getId, borrowIdList);
        borrowMapper.update(updateWrapper);
    }

    @Override
    @Transactional
    public void updateReservedBorrows() {
        log.info("[log] 将状态为'已预约'并且超过预约日期的借阅记录的状态更新为'预约已失效'，并更新对应书籍库存");
        // 查询当前日期超过预约日期的借阅记录
        LambdaQueryWrapper<Borrow> queryWrapper = new LambdaQueryWrapper<Borrow>()
                .select(Borrow::getId, Borrow::getIsbn)
                .eq(Borrow::getStatus, BorrowStatusConstant.RESERVED)
                .lt(Borrow::getReserveDate, LocalDate.now());
        List<Borrow> borrows = borrowMapper.selectList(queryWrapper);

        if (borrows.isEmpty()) return;

        // 将这些借阅记录的状态更改为'预约已失效'
        List<String> borrowIds = borrows.stream().map(Borrow::getId).collect(Collectors.toList());
        LambdaUpdateWrapper<Borrow> updateWrapper1 = new LambdaUpdateWrapper<Borrow>()
                .set(Borrow::getStatus, BorrowStatusConstant.RESERVE_OVERDUE)
                .in(Borrow::getId, borrowIds);
        borrowMapper.update(updateWrapper1);
        // 并更改书籍库存 当前库存+1
        List<String> isbnList = borrows.stream().map(Borrow::getIsbn).collect(Collectors.toList());
        LambdaUpdateWrapper<Book> updateWrapper2 = new UpdateWrapper<Book>()
                .setSql("stock=stock+1")
                .lambda()
                .in(Book::getIsbn, isbnList);
        bookMapper.update(updateWrapper2);
    }

}
