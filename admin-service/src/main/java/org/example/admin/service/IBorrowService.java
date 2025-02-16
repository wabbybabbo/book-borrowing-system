package org.example.admin.service;

import com.baomidou.mybatisplus.extension.service.IService;
import org.example.admin.entity.Borrow;
import org.example.admin.pojo.dto.ReturnRegisterDTO;
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
     * @param pageQuery {@link PageQuery}
     * @return {@link PageResult<Borrow>}
     */
    PageResult<Borrow> pageQuery(PageQuery pageQuery);

    /**
     * 查询用户的所有借阅记录
     *
     * @param id 用户ID
     * @return {@link List<Borrow>}
     */
    List<Borrow> getBorrows(String id);

    /**
     * 归还登记
     *
     * @param returnRegisterDTO {@link ReturnRegisterDTO}
     */
    void returnRegister(ReturnRegisterDTO returnRegisterDTO);

    /**
     * 借阅登记
     *
     * @param id 书籍借阅记录ID
     */
    void borrowRegister(String id);

    /**
     * 将状态为'借阅中'并且超过预计归还日期的借阅记录的状态更新为'未按时归还'
     */
    void updateBorrowingBorrows();

    /**
     * 将状态为'已预约'并且超过预约日期的借阅记录的状态更新为'预约已失效'，并更新对应书籍库存
     */
    void updateReservedBorrows();

}
