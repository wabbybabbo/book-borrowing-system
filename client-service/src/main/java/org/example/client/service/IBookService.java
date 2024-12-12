package org.example.client.service;

import com.baomidou.mybatisplus.extension.service.IService;
import org.example.client.pojo.entity.Book;
import org.example.client.pojo.query.PageQuery;
import org.example.client.pojo.vo.BookVO;
import org.example.common.result.PageResult;

/**
 * <p>
 * 图书表 服务类
 * </p>
 *
 * @author wabbybabbo
 * @since 2024-04-07
 */
public interface IBookService extends IService<Book> {

    /**
     * 分页查询图书
     *
     * @param pageQuery 分页查询时传递的数据模型
     * @return 图书列表
     */
    PageResult<BookVO> pageQuery(PageQuery pageQuery);

}
