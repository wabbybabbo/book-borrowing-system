package org.example.client.service;

import com.baomidou.mybatisplus.extension.service.IService;
import org.example.client.entity.Book;
import org.example.client.pojo.query.PageQuery;
import org.example.client.pojo.vo.BookVO;
import org.example.common.result.PageResult;

/**
 * <p>
 * 书籍信息表 服务类
 * </p>
 *
 * @author zhengjunpeng
 * @since 2024-04-07
 */
public interface IBookService extends IService<Book> {

    /**
     * 分页查询书籍信息
     *
     * @param pageQuery {@link PageQuery}
     * @return 书籍信息列表
     */
    PageResult<BookVO> pageQuery(PageQuery pageQuery);

}
