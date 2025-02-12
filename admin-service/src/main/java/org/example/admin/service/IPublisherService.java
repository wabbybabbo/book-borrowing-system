package org.example.admin.service;

import org.example.admin.entity.Publisher;
import org.example.admin.entity.Publisher;
import com.baomidou.mybatisplus.extension.service.IService;
import org.example.admin.pojo.dto.CreatePublisherDTO;
import org.example.admin.pojo.dto.UpdatePublisherDTO;
import org.example.admin.pojo.query.PageQuery;
import org.example.admin.pojo.vo.PublisherVO;
import org.example.common.result.PageResult;

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
     * 新建出版社信息
     *
     * @param createPublisherDTO {@link CreatePublisherDTO}
     */
    void createPublisher(CreatePublisherDTO createPublisherDTO);

    /**
     * 查询所有出版社信息
     *
     * @return {@link List <   PublisherVO   >}
     */
    List<PublisherVO> getPublishers();

    /**
     * 更改出版社信息
     *
     * @param updatePublisherDTO {@link UpdatePublisherDTO}
     */
    void updatePublisher(UpdatePublisherDTO updatePublisherDTO);

    /**
     * 删除出版社信息
     *
     * @param id 出版社ID
     */
    void deletePublisher(String id);

    /**
     * 批量删除出版社信息
     *
     * @param ids 出版社ID列表
     */
    void batchDeletePublishers(List<String> ids);

    /**
     * 分页查询出版社信息
     *
     * @param pageQuery {@link PageQuery}
     * @return {@link PageResult < Publisher >}
     */
    PageResult<Publisher> pageQuery(PageQuery pageQuery);

}
