package org.example.common.constant;

/**
 * RabbitMQ的相关常量
 */
public class RabbitMQConstant {

    public static final String NOTICE_QUEUE = "bookstore.notice.";
    public static final String NOTICE_FANOUT_EXCHANGE = "bookstore.notice.fanout";

    public static final String REMINDER_QUEUE = "bookstore.reminder.";
    public static final String REMINDER_DIRECT_EXCHANGE = "bookstore.reminder.direct";

    public static final String STATISTIC_QUEUE = "bookstore.statistic.queue";
    public static final String STATISTIC_DIRECT_EXCHANGE = "bookstore.statistic.direct";
    public static final String STATISTIC_ROUTING_KEY = "statistic";

}
