package org.example.client.service;

import org.example.client.entity.Notice;
import com.baomidou.mybatisplus.extension.service.IService;
import org.example.client.pojo.query.PageQuery;
import org.example.client.pojo.vo.NoticeVO;
import org.example.common.result.PageResult;

import java.util.List;

/**
 * <p>
 * 用户的通知消息表 服务类
 * </p>
 *
 * @author zhengjunpeng
 * @since 2025-02-16
 */
public interface INoticeService extends IService<Notice> {

    /**
     * 分页查询用户的通知消息
     *
     * @param id        用户ID
     * @param pageQuery {@link PageQuery}
     * @return 用户通知消息列表
     */
    PageResult<NoticeVO> pageQuery(String id, PageQuery pageQuery);

    /**
     * 获取未读通知消息的数量
     *
     * @param id        用户ID
     * @return 未读通知消息的数量
     */
    Long getUnreadNoticeCount(String id);

    /**
     * 将用户的通知消息标为已读
     *
     * @param id 通知消息ID
     */
    void readNotice(String id);

    /**
     * 批量将用户的通知消息标为已读
     *
     * @param ids 通知消息ID列表
     */
    void batchReadNotices(List<String> ids);

    /**
     * 将用户的通知消息标为未读
     *
     * @param id 通知消息ID
     */
    void unreadNotice(String id);

    /**
     * 批量将用户的通知消息标为未读
     *
     * @param ids 通知消息ID列表
     */
    void batchUnreadNotices(List<String> ids);

    /**
     * 删除用户的通知消息
     *
     * @param id 通知消息ID
     */
    void deleteNotice(String id);

    /**
     * 批量删除用户的通知消息
     *
     * @param ids 通知消息ID列表
     */
    void batchDeleteNotices(List<String> ids);

    /**
     * 新增用户的通知消息
     *
     * @param userId 用户ID
     * @param systemNoticeId 系统通知消息ID
     */
    void createNotice(String userId, String systemNoticeId);

}
