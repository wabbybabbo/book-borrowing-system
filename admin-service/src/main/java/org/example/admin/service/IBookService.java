package org.example.admin.service;

import com.baomidou.mybatisplus.extension.service.IService;
import org.example.admin.pojo.dto.CreateBookDTO;
import org.example.admin.pojo.dto.UpdateBookDTO;
import org.example.admin.entity.Book;
import org.example.admin.pojo.query.PageQuery;
import org.example.admin.pojo.vo.BookVO;
import org.example.common.result.PageResult;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

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
     * @param pageQuery 分页查询时传递的数据模型
     * @return 书籍信息列表
     */
    PageResult<BookVO> pageQuery(PageQuery pageQuery);

    /**
     * 新建书籍信息
     *
     * @param file          书籍封面图片文件
     * @param createBookDTO 新建书籍信息时传递的数据模型
     */
    void createBook(MultipartFile file, CreateBookDTO createBookDTO);

    /**
     * 更改书籍信息
     *
     * @param file          书籍封面图片文件
     * @param updateBookDTO 更改书籍信息时传递的数据模型
     */
    void updateBook(MultipartFile file, UpdateBookDTO updateBookDTO);

    /**
     * 删除书籍信息
     *
     * @param id 书籍ID
     */
    void deleteBook(String id);

    /**
     * 批量删除书籍信息
     *
     * @param ids 书籍ID列表
     */
    void batchDeleteBooks(List<String> ids);

}
