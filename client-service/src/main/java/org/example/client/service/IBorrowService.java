package org.example.client.service;

import com.baomidou.mybatisplus.extension.service.IService;
import org.example.client.pojo.dto.CreateBorrowDTO;
import org.example.client.entity.Borrow;
import org.example.client.pojo.query.PageQuery;
import org.example.client.pojo.vo.BorrowVO;
import org.example.common.result.PageResult;

import java.util.List;

/**
 * <p>
 * 借阅记录表 服务类
 * </p>
 *
 * @author zhengjunpeng
 * @since 2024-04-07
 */
public interface IBorrowService extends IService<Borrow> {

    /**
     * 分页查询用户的借阅记录
     *
     * @param pageQuery 分页查询时传递的数据模型
     * @param id        用户ID
     * @return 借阅记录列表
     */
    PageResult<BorrowVO> pageQuery(PageQuery pageQuery, String id);

    /**
     * 查询用户所有的借阅记录
     *
     * @param id 用户ID
     * @return 用户的借阅记录列表
     */
    List<BorrowVO> getBorrows(String id);

    /**
     * 新增用户的借阅记录
     *
     * @param createBorrowDTO 新增书籍借阅记录时传递的数据模型
     * @param id              用户ID
     */
    void createBorrow(CreateBorrowDTO createBorrowDTO, String id);

    /**
     * 取消书籍借阅预约
     *
     * @param id 借阅记录ID
     */
    void cancelBorrow(Long id);

    /**
     * 删除书籍借阅记录
     *
     * @param id 借阅记录ID
     */
    void deleteBorrow(Long id);
}
