package org.example.client.service;

import org.example.client.entity.Publisher;
import com.baomidou.mybatisplus.extension.service.IService;
import org.example.client.pojo.vo.PublisherVO;

import java.util.List;

/**
 * <p>
 * 出版社信息表 服务类
 * </p>
 *
 * @author zhengjunpeng
 * @since 2025-02-13
 */
public interface IPublisherService extends IService<Publisher> {

    /**
     * 查询所有出版社名称
     *
     * @return {@link List<PublisherVO>}
     */
    List<PublisherVO> getPublishers();

}
