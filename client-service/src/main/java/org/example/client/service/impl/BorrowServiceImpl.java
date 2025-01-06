package org.example.client.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.client.entity.Book;
import org.example.client.entity.Borrow;
import org.example.client.mapper.BookMapper;
import org.example.client.mapper.BorrowMapper;
import org.example.client.pojo.dto.CreateBorrowDTO;
import org.example.client.pojo.query.PageQuery;
import org.example.client.pojo.vo.BorrowVO;
import org.example.client.service.IBorrowService;
import org.example.common.constant.BorrowStatusConstant;
import org.example.common.constant.MessageConstant;
import org.example.common.exception.CheckException;
import org.example.common.exception.NotAllowedException;
import org.example.common.exception.NotFoundException;
import org.example.common.result.PageResult;
import org.springframework.jdbc.BadSqlGrammarException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.Objects;
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

    @Override
    public PageResult<BorrowVO> pageQuery(String id, PageQuery pageQuery) {
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
        LambdaQueryWrapper<Borrow> lambdaQueryWrapper = queryWrapper.lambda()
                .select(Borrow::getId, Borrow::getStatus, Borrow::getBookName, Borrow::getIsbn, Borrow::getReserveDate, Borrow::getReturnDate, Borrow::getUpdateTime)
                .eq(Borrow::getUserId, id);
        // 分页查询
        try {
            borrowMapper.selectPage(page, lambdaQueryWrapper);
        } catch (BadSqlGrammarException e) {
            log.error("[log] 借阅记录分页查询失败 BadSqlGrammarException: {}, msg: {}", e.getMessage(), MessageConstant.FIELD_NOT_FOUND);
            throw new NotFoundException(MessageConstant.FIELD_NOT_FOUND);
        }
        List<Borrow> records = page.getRecords();

        //转化为VO
        List<BorrowVO> borrowVOList = records.stream().map(borrow -> {
            BorrowVO borrowVO = new BorrowVO();
            BeanUtil.copyProperties(borrow, borrowVO);
            if (!borrowVO.getStatus().equals(BorrowStatusConstant.RETURNED)) {
                borrowVO.setUpdateTime(null);
            }
            return borrowVO;
        }).collect(Collectors.toList());

        return PageResult.<BorrowVO>builder()
                .total(page.getTotal())
                .pages(page.getPages())
                .records(borrowVOList)
                .build();
    }

    @Override
    public List<BorrowVO> getBorrows(String id) {
        // 构建查询条件
        LambdaQueryWrapper<Borrow> queryWrapper = new LambdaQueryWrapper<Borrow>()
                .select(Borrow::getId, Borrow::getStatus, Borrow::getBookName, Borrow::getIsbn, Borrow::getReserveDate, Borrow::getReturnDate, Borrow::getUpdateTime)
                .eq(Borrow::getUserId, id);
        // 查询用户的借阅记录
        List<Borrow> borrows = borrowMapper.selectList(queryWrapper);
        // 转化为BorrowVO
        return borrows.stream().map(borrow -> {
            BorrowVO borrowVO = new BorrowVO();
            BeanUtil.copyProperties(borrow, borrowVO);
            if (!borrowVO.getStatus().equals(BorrowStatusConstant.RETURNED)) {
                borrowVO.setUpdateTime(null);
            }
            return borrowVO;
        }).collect(Collectors.toList());
    }

    @Override
    @Transactional
    public void createBorrow(String id, CreateBorrowDTO createBorrowDTO) {
        // 检查参数是否合法
        LocalDate reserveDate = createBorrowDTO.getReserveDate();
        LocalDate returnDate = createBorrowDTO.getReturnDate();
        if (returnDate.isBefore(reserveDate)) {
            log.info("[log] 参数检查不通过 reserveDate: {}, returnDate: {}, msg: {}", reserveDate, returnDate, MessageConstant.RETURN_DATE_BEFORE_RESERVATION);
            throw new CheckException(MessageConstant.RETURN_DATE_BEFORE_RESERVATION);
        }
        // 查询书籍库存
        String isbn = createBorrowDTO.getIsbn();
        LambdaQueryWrapper<Book> queryWrapper = new LambdaQueryWrapper<Book>()
                .select(Book::getName, Book::getStock)
                .eq(Book::getIsbn, isbn);
        Book book = bookMapper.selectOne(queryWrapper);
        if (Objects.isNull(book)) {
            throw new NotFoundException(MessageConstant.BOOK_NOT_FOUND);
        }
        if (book.getStock() == 0) {
            throw new CheckException(MessageConstant.BOOK_STOCK_NOT_ENOUGH);
        }
        // 更改书籍库存 当前库存-1
        LambdaUpdateWrapper<Book> updateWrapper = new LambdaUpdateWrapper<Book>()
                .setSql("stock=stock-1")
                .eq(Book::getIsbn, isbn);
        bookMapper.update(updateWrapper);
        // 新增用户借阅记录
        Borrow borrow = new Borrow();
        BeanUtil.copyProperties(createBorrowDTO, borrow);
        borrow.setBookName(book.getName());
        borrow.setUserId(id);
        borrowMapper.insert(borrow);
    }

    @Override
    public void deleteBorrow(String id) {
        // 查询该借阅记录的借阅状态
        LambdaQueryWrapper<Borrow> queryWrapper = new LambdaQueryWrapper<Borrow>()
                .select(Borrow::getIsbn, Borrow::getStatus)
                .eq(Borrow::getId, id);
        Borrow borrow = borrowMapper.selectOne(queryWrapper);
        if (Objects.isNull(borrow)) {
            throw new NotFoundException(MessageConstant.BORROW_NOT_FOUND);
        }

        String status = borrow.getStatus();
        if (status.equals(BorrowStatusConstant.CANCELLED)
                || status.equals(BorrowStatusConstant.RETURNED)) {
            // 删除用户的借阅记录
            borrowMapper.deleteById(id);
        } else {
            throw new NotAllowedException(MessageConstant.DELETION_IS_NOT_ALLOWED);
        }
    }

    @Override
    @Transactional
    public void cancelBorrow(String id) {
        // 查询该借阅记录的借阅状态
        LambdaQueryWrapper<Borrow> queryWrapper = new LambdaQueryWrapper<Borrow>()
                .select(Borrow::getIsbn, Borrow::getStatus)
                .eq(Borrow::getId, id);
        Borrow borrow = borrowMapper.selectOne(queryWrapper);
        if (Objects.isNull(borrow)) {
            throw new NotFoundException(MessageConstant.BORROW_NOT_FOUND);
        }

        String status = borrow.getStatus();
        if (status.equals(BorrowStatusConstant.RESERVED)) {
            // 将该借阅记录的状态设置为“已取消”
            borrow.setId(id);
            borrow.setStatus(BorrowStatusConstant.CANCELLED);
            borrowMapper.updateById(borrow);
            // 更改书籍库存 当前库存+1
            LambdaUpdateWrapper<Book> updateWrapper = new LambdaUpdateWrapper<Book>()
                    .setSql("stock=stock+1")
                    .eq(Book::getIsbn, borrow.getIsbn());
            bookMapper.update(updateWrapper);
        } else {
            log.info("[log] 取消书籍借阅预约失败 id: {}, msg: {}", id, MessageConstant.CANCELLATION_IS_NOT_ALLOWED);
            throw new NotAllowedException(MessageConstant.CANCELLATION_IS_NOT_ALLOWED);
        }
    }

    @Override
    public void batchDeleteBorrows(List<String> ids) {
        int updates = borrowMapper.deleteBatchIds(ids);
        if (updates == 0) {
            log.error("[log] 批量删除书籍借阅记录失败 msg: {}", MessageConstant.BORROW_NOT_FOUND);
            throw new NotFoundException(MessageConstant.BORROW_NOT_FOUND);
        }
    }

}
