package org.example.admin.task;

import cn.hutool.extra.mail.MailUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.admin.entity.Book;
import org.example.admin.entity.Borrow;
import org.example.admin.entity.User;
import org.example.admin.mapper.BookMapper;
import org.example.admin.mapper.BorrowMapper;
import org.example.admin.mapper.UserMapper;
import org.example.common.constant.BorrowStatusConstant;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 定时任务类，定时处理借阅记录
 */
@Slf4j
@RequiredArgsConstructor
@Component
public class BorrowTask {

    private final BorrowMapper borrowMapper;
    private final BookMapper bookMapper;
    private final UserMapper userMapper;

    /**
     * 定时处理状态为'借阅中'的借阅记录
     */
    @Scheduled(cron = "0 0 0 * * ?") //每天晚上12点触发一次
    public void processBorrow() {
        log.info("[log] 定时处理状态为'借阅中'的借阅记录");
        // 查询当前日期超过预计归还日期的借阅记录
        LambdaQueryWrapper<Borrow> queryWrapper1 = new LambdaQueryWrapper<Borrow>()
                .select(Borrow::getId, Borrow::getUserId)
                .eq(Borrow::getStatus, BorrowStatusConstant.BORROW)
                .lt(Borrow::getReturnDate, LocalDate.now());
        List<Borrow> borrowList = borrowMapper.selectList(queryWrapper1);

        if (borrowList.isEmpty()) return;

        // 将这些借阅记录的借阅状态更改为'未按时归还'
        List<String> borrowIdList = borrowList.stream().map(Borrow::getId).collect(Collectors.toList());
        LambdaUpdateWrapper<Borrow> updateWrapper = new LambdaUpdateWrapper<Borrow>()
                .set(Borrow::getStatus, BorrowStatusConstant.RETURN_OVERDUE)
                .in(Borrow::getId, borrowIdList);
        borrowMapper.update(updateWrapper);

        // 通过邮件的方式提醒用户归还借阅逾期的书籍
        List<String> userIdList = borrowList.stream().map(Borrow::getUserId).collect(Collectors.toList());
        LambdaQueryWrapper<User> queryWrapper2 = new LambdaQueryWrapper<User>()
                .select(User::getEmail)
                .in(User::getId, userIdList);
        List<String> emailList = userMapper.selectList(queryWrapper2).stream().map(User::getEmail).toList();
        for (String email : emailList) {
            //todo 邮件内容中加上“借阅书籍名称”、“ISBN”、“借阅日期”、“预计归还日期”
            MailUtil.send(email, "【书店借阅平台】", "您有未归还的借阅书籍，请尽快归还。", true);
        }
    }

    /**
     * 定时处理状态为'已预约'的借阅记录
     */
    @Transactional
    @Scheduled(cron = "0 0 0 * * ?") //每天晚上12点触发一次
    public void processReserve() {
        log.info("[log] 定时处理状态为'已预约'的借阅记录");
        // 查询当前日期超过预约日期的借阅记录
        LambdaQueryWrapper<Borrow> queryWrapper = new LambdaQueryWrapper<Borrow>()
                .select(Borrow::getId, Borrow::getIsbn)
                .eq(Borrow::getStatus, BorrowStatusConstant.RESERVED)
                .lt(Borrow::getReserveDate, LocalDate.now());
        List<Borrow> borrows = borrowMapper.selectList(queryWrapper);

        if (borrows.isEmpty()) return;

        // 将这些借阅记录的借阅状态更改为'预约已失效'
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

