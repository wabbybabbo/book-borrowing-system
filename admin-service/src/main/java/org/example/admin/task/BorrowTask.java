package org.example.admin.task;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.admin.mapper.BookMapper;
import org.example.admin.mapper.BorrowMapper;
import org.example.admin.entity.Book;
import org.example.admin.entity.Borrow;
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

    /**
     * 定时处理状态为'借阅中'的借阅记录
     */
    @Scheduled(cron = "0 0 0 * * ?") //每天晚上12点触发一次
    public void processBorrow() {
        log.info("[log] 定时处理状态为'借阅中'的借阅记录");
        // 查询当前日期超过预计归还日期的借阅记录
        QueryWrapper<Borrow> queryWrapper = new QueryWrapper<Borrow>()
                .select("id")
                .eq("status", BorrowStatusConstant.BORROW)
                .lt("return_date", LocalDate.now());
        List<Borrow> borrows = borrowMapper.selectList(queryWrapper);

        if(borrows.isEmpty()) return;

        // 将这些借阅记录的借阅状态更改为'未按时归还'
        List<Long> borrowIds = borrows.stream().map(Borrow::getId).collect(Collectors.toList());
        UpdateWrapper<Borrow> updateWrapper = new UpdateWrapper<Borrow>()
                .set("status", BorrowStatusConstant.RETURN_OVERDUE)
                .in("id", borrowIds);
        borrowMapper.update(updateWrapper);
    }

    /**
     * 定时处理状态为'已预约'的借阅记录
     */
    @Transactional
    @Scheduled(cron = "0 0 0 * * ?") //每天晚上12点触发一次
    public void processReserve() {
        log.info("[log] 定时处理状态为'已预约'的借阅记录");
        // 查询当前日期超过预约日期的借阅记录
        QueryWrapper<Borrow> queryWrapper = new QueryWrapper<Borrow>()
                .select("id", "isbn")
                .eq("status", BorrowStatusConstant.RESERVED)
                .lt("reserve_date", LocalDate.now());
        List<Borrow> borrows = borrowMapper.selectList(queryWrapper);

        if(borrows.isEmpty()) return;

        // 将这些借阅记录的借阅状态更改为'预约已失效'
        List<Long> borrowIds = borrows.stream().map(Borrow::getId).collect(Collectors.toList());
        UpdateWrapper<Borrow> updateWrapper1 = new UpdateWrapper<Borrow>()
                .set("status", BorrowStatusConstant.RESERVE_OVERDUE)
                .in("id", borrowIds);
        borrowMapper.update(updateWrapper1);
        // 并更改书籍库存 当前库存+1
        List<String> isbns = borrows.stream().map(Borrow::getIsbn).collect(Collectors.toList());
        UpdateWrapper<Book> updateWrapper2 = new UpdateWrapper<Book>()
                .setSql("stock=stock+1")
                .in("isbn", isbns);
        bookMapper.update(updateWrapper2);
    }

}

