package org.example.admin.service;

import com.baomidou.mybatisplus.extension.service.IService;
import org.example.admin.pojo.dto.ReturnRegisterDTO;
import org.example.admin.entity.Borrow;
import org.example.admin.pojo.query.PageQuery;
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
     * 分页查询书籍信息
     *
     * @param pageQuery 分页查询时传递的数据模型
     * @return 书籍信息列表
     */
    PageResult<Borrow> pageQuery(PageQuery pageQuery);

    /**
     * 查询用户的借阅记录
     *
     * @param id 用户ID
     * @return 用户的借阅记录列表
     */
    List<Borrow> getBorrows(Long id);

    /**
     * 归还登记
     *
     * @param returnRegisterDTO 用户归还借阅的书籍时传递的数据模型
     */
    void returnRegister(ReturnRegisterDTO returnRegisterDTO);

    /**
     * 借阅登记
     *
     * @param id 书籍借阅记录ID
     */
    void borrowRegister(Long id);

}
