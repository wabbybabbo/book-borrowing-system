package org.example.admin.listener;

import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.admin.mapper.BorrowStatisticMapper;
import org.example.admin.pojo.entity.BorrowStatistic;
import org.springframework.amqp.rabbit.annotation.Exchange;
import org.springframework.amqp.rabbit.annotation.Queue;
import org.springframework.amqp.rabbit.annotation.QueueBinding;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import java.time.LocalDate;

@Slf4j
@RequiredArgsConstructor
@Component
public class BorrowStatisticListener {

    private final BorrowStatisticMapper borrowStatisticMapper;

    @RabbitListener(bindings = @QueueBinding(
            value = @Queue(name = "direct.queue"),
            exchange = @Exchange(name = "amq.direct"),
            key = {"increment"}
    ))
    public void increment() {
        log.info("[log] increment");
        UpdateWrapper<BorrowStatistic> updateWrapper = new UpdateWrapper<BorrowStatistic>()
                .setSql("quantity=quantity+1")
                .eq("date", LocalDate.now());

        int updates = borrowStatisticMapper.update(updateWrapper);

        if (0 == updates) {
            BorrowStatistic borrowStatistic = new BorrowStatistic();
            borrowStatistic.setDate(LocalDate.now());
            borrowStatistic.setQuantity(1);
            borrowStatisticMapper.insert(borrowStatistic);
        }
    }

}
