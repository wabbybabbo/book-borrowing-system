package org.example.admin.service;

import com.baomidou.mybatisplus.extension.service.IService;
import org.example.admin.entity.SystemNotice;
import org.example.admin.pojo.dto.CreateSystemNoticeDTO;
import org.example.admin.pojo.dto.UpdateSystemNoticeDTO;
import org.example.admin.pojo.query.PageQuery;
import org.example.common.result.PageResult;

import java.util.List;

/**
 * <p>
 * 系统通知消息表 服务类
 * </p>
 *
 * @author zhengjunpeng
 * @since 2025-02-16
 */
public interface ISystemNoticeService extends IService<SystemNotice> {

    /**
     * 分页查询系统通知消息
     *
     * @param pageQuery {@link PageQuery}
     * @return {@link PageResult<SystemNotice>}
     */
    PageResult<SystemNotice> pageQuery(PageQuery pageQuery);
    
    /**
     * 新建系统通知消息
     *
     * @param createSystemNoticeDTO {@link CreateSystemNoticeDTO}
     */
    void createSystemNotice(CreateSystemNoticeDTO createSystemNoticeDTO);

    /**
     * 更改系统通知消息
     *
     * @param updateSystemNoticeDTO {@link UpdateSystemNoticeDTO}
     */
    void updateSystemNotice(UpdateSystemNoticeDTO updateSystemNoticeDTO);

    /**
     * 删除系统通知消息
     *
     * @param id 通知消息ID
     */
    void deleteSystemNotice(String id);

    /**
     * 批量删除系统通知消息
     *
     * @param ids 系统通知消息ID列表
     */
    void batchDeleteSystemNotices(List<String> ids);

    /**
     * 发布系统通知消息给所有用户
     *
     * @param id 系统通知消息ID
     */
    void publishSystemNotice(String id);

}
