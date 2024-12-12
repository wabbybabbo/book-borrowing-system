package org.example.admin.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.example.admin.mapper.FavoriteMapper;
import org.example.admin.pojo.entity.Favorite;
import org.example.admin.service.IFavoriteService;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 收藏记录表 服务实现类
 * </p>
 *
 * @author wabbybabbo
 * @since 2024-04-07
 */
@Service
public class FavoriteServiceImpl extends ServiceImpl<FavoriteMapper, Favorite> implements IFavoriteService {

}
