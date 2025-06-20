package org.example.admin.service;

import com.baomidou.mybatisplus.extension.service.IService;
import org.example.admin.entity.BorrowStatistic;
import org.example.admin.pojo.query.PageQuery;
import org.example.common.result.PageResult;

/**
 * <p>
 * 借阅数量统计表 服务类
 * </p>
 *
 * @author zhengjunpeng
 * @since 2024-05-05
 */
public interface IBorrowStatisticService extends IService<BorrowStatistic> {

    /**
     * 分页查询借阅数量统计数据
     *
     * @param pageQuery {@link PageQuery}
     * @return {@link PageResult<BorrowStatistic>}
     */
    PageResult<BorrowStatistic> pageQuery(PageQuery pageQuery);

}
