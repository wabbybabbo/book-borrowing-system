package org.example.admin.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Select;
import org.example.admin.entity.Publisher;
import org.example.admin.pojo.vo.PublisherVO;

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
     * 查询所有出版社
     *
     * @return 出版社列表
     */
    @Select("select id, name from publisher")
    List<PublisherVO> getPublishers();

}
