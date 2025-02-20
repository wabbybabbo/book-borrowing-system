package org.example.admin.listener;

import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.admin.entity.BorrowStatistic;
import org.example.admin.mapper.BorrowStatisticMapper;
import org.example.common.constant.RabbitMQConstant;
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
            value = @Queue(name = RabbitMQConstant.STATISTIC_QUEUE),
            exchange = @Exchange(name = RabbitMQConstant.STATISTIC_DIRECT_EXCHANGE),
            key = {RabbitMQConstant.STATISTIC_ROUTING_KEY}))
    public void statistic() {
        log.info("[log] 统计借阅数量 今日借阅数量+1");
        LambdaUpdateWrapper<BorrowStatistic> updateWrapper = new UpdateWrapper<BorrowStatistic>()
                .setSql("quantity=quantity+1")
                .lambda()
                .eq(BorrowStatistic::getDate, LocalDate.now());

        int updates = borrowStatisticMapper.update(updateWrapper);

        if (updates == 0) {
            BorrowStatistic borrowStatistic = new BorrowStatistic();
            borrowStatistic.setDate(LocalDate.now());
            borrowStatistic.setQuantity(1);
            borrowStatisticMapper.insert(borrowStatistic);
        }
    }

}
