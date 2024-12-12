package org.example.client.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.example.client.mapper.FavoriteMapper;
import org.example.client.pojo.entity.Favorite;
import org.example.client.service.IFavoriteService;
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
