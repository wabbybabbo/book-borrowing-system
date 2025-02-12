package org.example.admin.service.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.extra.mail.MailUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.admin.entity.Book;
import org.example.admin.entity.Borrow;
import org.example.admin.entity.User;
import org.example.admin.mapper.BookMapper;
import org.example.admin.mapper.BorrowMapper;
import org.example.admin.mapper.UserMapper;
import org.example.admin.pojo.dto.RemindDTO;
import org.example.admin.pojo.dto.ReturnRegisterDTO;
import org.example.admin.pojo.query.PageQuery;
import org.example.admin.service.IBorrowService;
import org.example.common.constant.BorrowStatusConstant;
import org.example.common.constant.MessageConstant;
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
    private final UserMapper userMapper;
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
            log.info("[log] Records {}", page.getRecords());
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
        // 构建查询条件
        LambdaQueryWrapper<Borrow> queryWrapper = new LambdaQueryWrapper<Borrow>()
                .eq(Borrow::getUserId, id);
        // 查询用户的借阅记录
        List<Borrow> borrows = borrowMapper.selectList(queryWrapper);

        return borrows.stream().peek(borrow -> {
            if (!borrow.getStatus().equals(BorrowStatusConstant.RETURNED)) {
                borrow.setUpdateTime(null);
            }
        }).collect(Collectors.toList());
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
        borrow.setStatus(BorrowStatusConstant.BORROW);
        // 更改借阅状态
        borrowMapper.updateById(borrow);
        // 发送消息，异步调用统计方法
        rabbitTemplate.convertAndSend("amq.direct", "statistic", "");
    }

    @Override
    public void remindByBorrowStatus(RemindDTO remindDTO) {
        LambdaQueryWrapper<User> queryWrapper = new LambdaQueryWrapper<User>()
                .select(User::getEmail)
                .eq(User::getId, remindDTO.getUserId());
        String email = userMapper.selectOne(queryWrapper).getEmail();
        String status = remindDTO.getStatus();
        String bookName = remindDTO.getBookName();
        String isbn = remindDTO.getIsbn();
        LocalDate returnDate = remindDTO.getReturnDate();
        // todo 如果用户在线（用户表新增‘是否在线’状态）则发送到用户界面，否则发送到用户邮箱
        // 根据借阅记录状态发送相应的提醒信息给用户
        if (status.equals(BorrowStatusConstant.RESERVED)) {
            LocalDate reserveDate = remindDTO.getReserveDate();
            String content = "<p>您有以下借阅预约，请注意预约日期，在预约日期之前前往书店进行借阅登记。</p>" +
                    "<div style=\"width: fit-content; font-family: sans-serif;\">" +
                    "  <h1 style=\"padding: 5px; color: #353b48; font: 18px system-ui; text-align: center; border-bottom: 1.5px solid #bdc3c7;\">借阅预约信息</h1>" +
                    "  <p>书籍名称：" + bookName + "</p>" +
                    "  <p>ISBN：" + isbn + "</p>" +
                    "  <p>借阅预约日期：<span style=\"color: rgb(128, 96, 96); text-decoration: rgb(128, 96, 96) underline\">" + reserveDate + "</span></p>" +
                    "</div>";
            MailUtil.send(email, "【书店借阅平台】", content, true);
        }
        if (status.equals(BorrowStatusConstant.BORROW)) {

        }
        if (status.equals(BorrowStatusConstant.RETURN_OVERDUE)) {

        }
    }

}
