package org.example.admin.service;

import com.baomidou.mybatisplus.extension.service.IService;
import org.example.admin.pojo.dto.CreateBookDTO;
import org.example.admin.pojo.dto.UpdateBookDTO;
import org.example.admin.pojo.entity.Book;
import org.example.admin.pojo.query.PageQuery;
import org.example.admin.pojo.vo.BookVO;
import org.example.common.result.PageResult;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

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

    /**
     * 新建图书
     *
     * @param file          图书封面图片文件
     * @param createBookDTO 新建图书时传递的数据模型
     */
    void createBook(MultipartFile file, CreateBookDTO createBookDTO);

    /**
     * 更改图书信息
     *
     * @param file          图书封面图片文件
     * @param updateBookDTO 更改图书信息时传递的数据模型
     */
    void updateBook(MultipartFile file, UpdateBookDTO updateBookDTO);

    /**
     * 删除图书
     *
     * @param id 图书ID
     */
    void deleteBook(Integer id);

    /**
     * 批量删除图书
     *
     * @param ids 图书ID列表
     */
    void batchDeleteBooks(List<Integer> ids);
}
