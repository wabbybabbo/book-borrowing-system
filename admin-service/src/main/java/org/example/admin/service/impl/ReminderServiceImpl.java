package org.example.admin.service.impl;

import cn.hutool.extra.mail.MailUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.admin.entity.Borrow;
import org.example.admin.entity.Reminder;
import org.example.admin.entity.User;
import org.example.admin.mapper.BorrowMapper;
import org.example.admin.mapper.ReminderMapper;
import org.example.admin.mapper.UserMapper;
import org.example.admin.pojo.dto.SendReminderDTO;
import org.example.admin.service.IReminderService;
import org.example.common.constant.BorrowStatusConstant;
import org.example.common.constant.MessageConstant;
import org.example.common.exception.NotAllowedException;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

/**
 * <p>
 * 用户提醒消息表 服务实现类
 * </p>
 *
 * @author zhengjunpeng
 * @since 2025-02-13
 */
@Slf4j
@RequiredArgsConstructor
@Service
public class ReminderServiceImpl extends ServiceImpl<ReminderMapper, Reminder> implements IReminderService {

    private final ReminderMapper reminderMapper;
    private final BorrowMapper borrowMapper;
    private final UserMapper userMapper;

    @Override
    public void sendReminder(SendReminderDTO sendReminderDTO) {
        String userId = sendReminderDTO.getUserId();
        String status = sendReminderDTO.getStatus();
        String bookName = sendReminderDTO.getBookName();
        String isbn = sendReminderDTO.getIsbn();
        LocalDate reserveDate = sendReminderDTO.getReserveDate();
        LocalDate returnDate = sendReminderDTO.getReturnDate();
        String title = null;
        String content = null;

        // 根据借阅记录状态发送相应的提醒信息给用户
        switch (status) {
            case BorrowStatusConstant.RESERVED -> {
                title = "借阅预约提醒";
                content = "<p>您有以下借阅预约，请注意预约日期，在预约日期之前前往书店进行借阅登记。</p>" +
                        "<div style=\"margin-left:1rem; width: fit-content; font-family: sans-serif;\">" +
                        "  <h1 style=\"padding: 5px; color: #353b48; font: 18px system-ui; text-align: center; border-bottom: 1.5px solid #bdc3c7;\">书籍借阅信息</h1>" +
                        "  <p>书籍名称：" + bookName + "</p>" +
                        "  <p>ISBN：" + isbn + "</p>" +
                        "  <p>借阅预约日期：<span style=\"color: rgb(128, 96, 96); text-decoration: rgb(128, 96, 96) underline\">" + reserveDate + "</span></p>" +
                        "  <p>预计归还日期：" + returnDate + "</p>" +
                        "</div>";
            }
            case BorrowStatusConstant.BORROWING -> {
                title = "借阅归还提醒";
                content = "<p>您有以下借阅记录，请注意归还日期，在归还日期之前前往书店进行归还登记。</p>" +
                        "<div style=\"margin-left:1rem; width: fit-content; font-family: sans-serif;\">" +
                        "  <h1 style=\"padding: 5px; color: #353b48; font: 18px system-ui; text-align: center; border-bottom: 1.5px solid #bdc3c7;\">书籍借阅信息</h1>" +
                        "  <p>书籍名称：" + bookName + "</p>" +
                        "  <p>ISBN：" + isbn + "</p>" +
                        "  <p>借阅预约日期：" + reserveDate + "</p>" +
                        "  <p>预计归还日期：<span style=\"color: rgb(128, 96, 96); text-decoration: rgb(128, 96, 96) underline\">" + returnDate + "</span></p>" +
                        "</div>";
            }
            case BorrowStatusConstant.RETURN_OVERDUE -> {
                title = "借阅逾期提醒";
                content = "<p>您有未按时归还书籍的借阅记录，请尽快前往书店进行归还登记！</p>" +
                        "<div style=\"margin-left:1rem; width: fit-content; font-family: sans-serif;\">" +
                        "  <h1 style=\"padding: 5px; color: #353b48; font: 18px system-ui; text-align: center; border-bottom: 1.5px solid #bdc3c7;\">书籍借阅信息</h1>" +
                        "  <p>书籍名称：" + bookName + "</p>" +
                        "  <p>ISBN：" + isbn + "</p>" +
                        "  <p>借阅预约日期：" + reserveDate + "</p>" +
                        "  <p>预计归还日期：<span style=\"color: rgb(128, 96, 96); text-decoration: rgb(128, 96, 96) underline\">" + returnDate + "</span></p>" +
                        "</div>";
            }
            default -> {
                throw new NotAllowedException(MessageConstant.REMIND_IS_NOT_ALLOWED);
            }
        }

        // 新增用户提醒消息
        Reminder reminder = new Reminder();
        reminder.setUserId(userId)
                .setTitle(title)
                .setContent(content);
        reminderMapper.insert(reminder);

        // 发送邮件给用户
        LambdaQueryWrapper<User> queryWrapper = new LambdaQueryWrapper<User>()
                .select(User::getEmail)
                .eq(User::getId, userId);
        String email = userMapper.selectOne(queryWrapper).getEmail();
        MailUtil.send(email, "【书店借阅平台】" + title, content, true);
    }

    @Override
    public void sendReservedReminders(long days) {
        // 查询状态为‘已预约’并且从今天到预约日期的时间间距<=days天的借阅记录
        LambdaQueryWrapper<Borrow> queryWrapper1 = new LambdaQueryWrapper<Borrow>()
                .select(Borrow::getUserId, Borrow::getBookName, Borrow::getIsbn, Borrow::getReserveDate, Borrow::getReturnDate)
                .eq(Borrow::getStatus, BorrowStatusConstant.RESERVED)
                .le(Borrow::getReserveDate, LocalDate.now().plusDays(days));
        List<Borrow> borrowList = borrowMapper.selectList(queryWrapper1);

        if (!borrowList.isEmpty()) {
            for (Borrow borrow : borrowList) {
                String title = "借阅预约提醒";
                String content = "<p>您有以下借阅预约，请注意预约日期，在预约日期之前前往书店进行借阅登记。</p>" +
                        "<div style=\"margin-left:1rem; width: fit-content; font-family: sans-serif;\">" +
                        "  <h1 style=\"padding: 5px; color: #353b48; font: 18px system-ui; text-align: center; border-bottom: 1.5px solid #bdc3c7;\">书籍借阅信息</h1>" +
                        "  <p>书籍名称：" + borrow.getBookName() + "</p>" +
                        "  <p>ISBN：" + borrow.getIsbn() + "</p>" +
                        "  <p>借阅预约日期：<span style=\"color: rgb(128, 96, 96); text-decoration: rgb(128, 96, 96) underline\">" + borrow.getReserveDate() + "</span></p>" +
                        "  <p>预计归还日期：" + borrow.getReturnDate() + "</p>" +
                        "</div>";

                // 新增用户提醒消息
                Reminder reminder = new Reminder();
                reminder.setUserId(borrow.getUserId())
                        .setTitle(title)
                        .setContent(content);
                reminderMapper.insert(reminder);

                // 发送提醒消息到用户邮箱
                LambdaQueryWrapper<User> queryWrapper2 = new LambdaQueryWrapper<User>()
                        .select(User::getEmail)
                        .eq(User::getId, borrow.getUserId());
                String email = userMapper.selectOne(queryWrapper2).getEmail();
                MailUtil.send(email, "【书店借阅平台】" + title, content, true);
            }
        }
    }

    @Override
    public void sendBorrowingReminders(long days) {
        // 查询状态为‘借阅中’并且从今天到预计归还日期的时间间距<=days天的借阅记录
        LambdaQueryWrapper<Borrow> queryWrapper1 = new LambdaQueryWrapper<Borrow>()
                .select(Borrow::getUserId, Borrow::getBookName, Borrow::getIsbn, Borrow::getReserveDate, Borrow::getReturnDate)
                .eq(Borrow::getStatus, BorrowStatusConstant.BORROWING)
                .le(Borrow::getReturnDate, LocalDate.now().plusDays(days));
        List<Borrow> borrowList = borrowMapper.selectList(queryWrapper1);

        if (!borrowList.isEmpty()) {
            for (Borrow borrow : borrowList) {
                String title = "借阅归还提醒";
                String content = "<p>您有以下借阅记录，请注意归还日期，在归还日期之前前往书店进行归还登记。</p>" +
                        "<div style=\"margin-left:1rem; width: fit-content; font-family: sans-serif;\">" +
                        "  <h1 style=\"padding: 5px; color: #353b48; font: 18px system-ui; text-align: center; border-bottom: 1.5px solid #bdc3c7;\">书籍借阅信息</h1>" +
                        "  <p>书籍名称：" + borrow.getBookName() + "</p>" +
                        "  <p>ISBN：" + borrow.getIsbn() + "</p>" +
                        "  <p>借阅预约日期：" + borrow.getReserveDate() + "</p>" +
                        "  <p>预计归还日期：<span style=\"color: rgb(128, 96, 96); text-decoration: rgb(128, 96, 96) underline\">" + borrow.getReturnDate() + "</span></p>" +
                        "</div>";

                // 新增用户提醒消息
                Reminder reminder = new Reminder();
                reminder.setUserId(borrow.getUserId())
                        .setTitle(title)
                        .setContent(content);
                reminderMapper.insert(reminder);

                // 发送提醒消息到用户邮箱
                LambdaQueryWrapper<User> queryWrapper2 = new LambdaQueryWrapper<User>()
                        .select(User::getEmail)
                        .eq(User::getId, borrow.getUserId());
                String email = userMapper.selectOne(queryWrapper2).getEmail();
                MailUtil.send(email, "【书店借阅平台】" + title, content, true);
            }
        }
    }

    @Override
    public void sendReserveOverdueReminders() {
        // 查询状态为‘预约逾期’的借阅记录
        LambdaQueryWrapper<Borrow> queryWrapper1 = new LambdaQueryWrapper<Borrow>()
                .select(Borrow::getUserId, Borrow::getBookName, Borrow::getIsbn, Borrow::getReserveDate, Borrow::getReturnDate)
                .eq(Borrow::getStatus, BorrowStatusConstant.RESERVE_OVERDUE)
                .eq(Borrow::getReserveDate, LocalDate.now().minusDays(1));
        List<Borrow> borrowList = borrowMapper.selectList(queryWrapper1);

        if (!borrowList.isEmpty()) {
            for (Borrow borrow : borrowList) {
                String title = "借阅预约逾期提醒";
                String content = "<p>您的借阅预约已经逾期失效了。</p>" +
                        "<div style=\"margin-left:1rem; width: fit-content; font-family: sans-serif;\">" +
                        "  <h1 style=\"padding: 5px; color: #353b48; font: 18px system-ui; text-align: center; border-bottom: 1.5px solid #bdc3c7;\">书籍借阅信息</h1>" +
                        "  <p>书籍名称：" + borrow.getBookName() + "</p>" +
                        "  <p>ISBN：" + borrow.getIsbn() + "</p>" +
                        "  <p>借阅预约日期：<span style=\"color: rgb(128, 96, 96); text-decoration: rgb(128, 96, 96) underline\">" + borrow.getReserveDate() + "</span></p>" +
                        "  <p>预计归还日期：" + borrow.getReturnDate() + "</p>" +
                        "</div>";

                // 新增用户提醒消息
                Reminder reminder = new Reminder();
                reminder.setUserId(borrow.getUserId())
                        .setTitle(title)
                        .setContent(content);
                reminderMapper.insert(reminder);

                // 发送提醒消息到用户邮箱
                LambdaQueryWrapper<User> queryWrapper2 = new LambdaQueryWrapper<User>()
                        .select(User::getEmail)
                        .eq(User::getId, borrow.getUserId());
                String email = userMapper.selectOne(queryWrapper2).getEmail();
                MailUtil.send(email, "【书店借阅平台】" + title, content, true);
            }
        }
    }

    @Override
    public void sendReturnOverdueReminders() {
        // 查询状态为‘归还逾期’的借阅记录
        LambdaQueryWrapper<Borrow> queryWrapper1 = new LambdaQueryWrapper<Borrow>()
                .select(Borrow::getUserId, Borrow::getBookName, Borrow::getIsbn, Borrow::getReserveDate, Borrow::getReturnDate)
                .eq(Borrow::getStatus, BorrowStatusConstant.RETURN_OVERDUE);
        List<Borrow> borrowList = borrowMapper.selectList(queryWrapper1);

        if (!borrowList.isEmpty()) {
            for (Borrow borrow : borrowList) {
                String title = "借阅逾期提醒";
                String content = "<p>您有未按时归还书籍的借阅记录，请尽快前往书店进行归还登记！</p>" +
                        "<div style=\"margin-left:1rem; width: fit-content; font-family: sans-serif;\">" +
                        "  <h1 style=\"padding: 5px; color: #353b48; font: 18px system-ui; text-align: center; border-bottom: 1.5px solid #bdc3c7;\">书籍借阅信息</h1>" +
                        "  <p>书籍名称：" + borrow.getBookName() + "</p>" +
                        "  <p>ISBN：" + borrow.getIsbn() + "</p>" +
                        "  <p>借阅预约日期：" + borrow.getReserveDate() + "</p>" +
                        "  <p>预计归还日期：<span style=\"color: rgb(128, 96, 96); text-decoration: rgb(128, 96, 96) underline\">" + borrow.getReturnDate() + "</span></p>" +
                        "</div>";

                // 新增用户提醒消息
                Reminder reminder = new Reminder();
                reminder.setUserId(borrow.getUserId())
                        .setTitle(title)
                        .setContent(content);
                reminderMapper.insert(reminder);

                // 发送提醒消息到用户邮箱
                LambdaQueryWrapper<User> queryWrapper2 = new LambdaQueryWrapper<User>()
                        .select(User::getEmail)
                        .eq(User::getId, borrow.getUserId());
                String email = userMapper.selectOne(queryWrapper2).getEmail();
                MailUtil.send(email, "【书店借阅平台】" + title, content, true);
            }
        }
    }

}
