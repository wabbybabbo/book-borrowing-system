package org.example.client.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.client.context.UserContext;
import org.example.client.mapper.BookMapper;
import org.example.client.mapper.BorrowMapper;
import org.example.client.pojo.dto.CreateBorrowDTO;
import org.example.client.pojo.entity.Book;
import org.example.client.pojo.entity.Borrow;
import org.example.client.pojo.query.PageQuery;
import org.example.client.pojo.vo.BorrowVO;
import org.example.client.service.IBorrowService;
import org.example.common.constant.BorrowStatusConstant;
import org.example.common.constant.MessageConstant;
import org.example.common.exception.CheckException;
import org.example.common.exception.NotAllowedException;
import org.example.common.exception.NotFoundException;
import org.example.common.result.PageResult;
import org.springframework.beans.BeanUtils;
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
 * @author wabbybabbo
 * @since 2024-04-07
 */
@Slf4j
@RequiredArgsConstructor
@Service
public class BorrowServiceImpl extends ServiceImpl<BorrowMapper, Borrow> implements IBorrowService {

    private final BorrowMapper borrowMapper;
    private final BookMapper bookMapper;

    @Override
    public PageResult<BorrowVO> pageQuery(PageQuery pageQuery) {
        // 构建分页查询条件
        Page<Borrow> page = pageQuery.toMpPage();
        QueryWrapper<Borrow> queryWrapper = new QueryWrapper<Borrow>()
                .select("id", "status", "book_name", "isbn", "reserve_date", "return_date", "update_time")
                .eq("user_id", UserContext.getUserId());
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
            borrowMapper.selectPage(page, queryWrapper);
        } catch (BadSqlGrammarException e) {
            log.error("[log] BadSqlGrammarException: {}", e.getMessage());
            throw new NotFoundException(MessageConstant.FIELD_NOT_FOUND);
        }
        List<Borrow> records = page.getRecords();

        //转化为VO
        List<BorrowVO> borrowVOList = records.stream().map(borrow -> {
            BorrowVO borrowVO = new BorrowVO();
            BeanUtils.copyProperties(borrow, borrowVO);
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
    public List<BorrowVO> getBorrows() {
        // 构建查询条件
        QueryWrapper<Borrow> queryWrapper = new QueryWrapper<Borrow>()
                .select("id", "status", "book_name", "isbn", "reserve_date", "return_date", "update_time")
                .eq("user_id", UserContext.getUserId());
        // 查询用户的借阅记录
        List<Borrow> borrows = borrowMapper.selectList(queryWrapper);
        // 转化为BorrowVO
        return borrows.stream().map(borrow -> {
            BorrowVO borrowVO = new BorrowVO();
            BeanUtils.copyProperties(borrow, borrowVO);
            if (!borrowVO.getStatus().equals(BorrowStatusConstant.RETURNED)) {
                borrowVO.setUpdateTime(null);
            }
            return borrowVO;
        }).collect(Collectors.toList());
    }

    @Override
    @Transactional
    public void createBorrow(CreateBorrowDTO createBorrowDTO) {
        String isbn = createBorrowDTO.getIsbn();
        LocalDate reserveDate = createBorrowDTO.getReserveDate();
        LocalDate returnDate = createBorrowDTO.getReturnDate();
        // 检查参数是否合法
        if (returnDate.isBefore(reserveDate)) {
            log.info("[log] CheckException: {}", MessageConstant.RETURN_DATE_BEFORE_RESERVATION);
            throw new CheckException(MessageConstant.RETURN_DATE_BEFORE_RESERVATION);
        }
        // 查询图书库存
        QueryWrapper<Book> queryWrapper = new QueryWrapper<Book>()
                .select("stock", "name")
                .eq("isbn", isbn);
        Book book = bookMapper.selectOne(queryWrapper);
        if (null == book) {
            throw new NotFoundException(MessageConstant.BOOK_NOT_FOUND);
        }
        if (0 == book.getStock()) {
            throw new CheckException(MessageConstant.BOOK_STOCK_NOT_ENOUGH);
        }
        // 更改图书库存 当前库存-1
        UpdateWrapper<Book> updateWrapper = new UpdateWrapper<Book>()
                .setSql("stock=stock-1")
                .eq("isbn", isbn);
        bookMapper.update(updateWrapper);
        // 新增用户借阅记录
        Borrow borrow = new Borrow();
        BeanUtils.copyProperties(createBorrowDTO, borrow);
        borrow.setBookName(book.getName());
        borrow.setUserId(UserContext.getUserId());
        borrowMapper.insert(borrow);
    }

    @Override
    public void deleteBorrow(Integer id) {
        // 查询该借阅记录的借阅状态
        QueryWrapper<Borrow> queryWrapper = new QueryWrapper<Borrow>()
                .select("isbn", "status")
                .eq("id", id);
        Borrow borrow = borrowMapper.selectOne(queryWrapper);
        if (null == borrow) {
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
    public void cancelBorrow(Integer id) {
        // 查询该借阅记录的借阅状态
        QueryWrapper<Borrow> queryWrapper = new QueryWrapper<Borrow>()
                .select("isbn", "status")
                .eq("id", id);
        Borrow borrow = borrowMapper.selectOne(queryWrapper);
        if (null == borrow) {
            throw new NotFoundException(MessageConstant.BORROW_NOT_FOUND);
        }

        String status = borrow.getStatus();
        if (status.equals(BorrowStatusConstant.RESERVED)) {
            // 将该借阅记录的状态设置为“已取消”
            borrow.setId(id);
            borrow.setStatus(BorrowStatusConstant.CANCELLED);
            borrowMapper.updateById(borrow);
            // 更改图书库存 当前库存+1
            UpdateWrapper<Book> updateWrapper = new UpdateWrapper<Book>()
                    .setSql("stock=stock+1")
                    .eq("isbn", borrow.getIsbn());
            bookMapper.update(updateWrapper);
        } else {
            throw new NotAllowedException(MessageConstant.CANCELLATION_IS_NOT_ALLOWED);
        }
    }

}
