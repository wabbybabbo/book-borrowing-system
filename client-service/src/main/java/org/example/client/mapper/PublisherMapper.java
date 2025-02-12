package org.example.client.mapper;

import org.apache.ibatis.annotations.Select;
import org.example.client.entity.Publisher;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.example.client.pojo.vo.PublisherVO;

import java.util.List;

/**
 * <p>
 * 出版社信息表 Mapper 接口
 * </p>
 *
 * @author zhengjunpeng
 * @since 2025-02-13
 */
public interface PublisherMapper extends BaseMapper<Publisher> {

    /**
     * 查询所有出版社名称
     *
     * @return 出版社名称列表
     */
    @Select("select id, name from publisher")
    List<PublisherVO> getPublishers();

}
