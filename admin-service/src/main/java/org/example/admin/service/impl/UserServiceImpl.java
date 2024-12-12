package org.example.admin.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.admin.mapper.UserMapper;
import org.example.admin.pojo.dto.CreateUserDTO;
import org.example.admin.pojo.dto.UserLoginDTO;
import org.example.admin.pojo.entity.User;
import org.example.admin.pojo.query.PageQuery;
import org.example.admin.pojo.vo.UserLoginVO;
import org.example.admin.service.IUserService;
import org.example.common.api.client.CommonClient;
import org.example.common.constant.*;
import org.example.common.exception.AlreadyExistsException;
import org.example.common.exception.CheckException;
import org.example.common.exception.NotFoundException;
import org.example.common.result.PageResult;
import org.springframework.beans.BeanUtils;
import org.springframework.jdbc.BadSqlGrammarException;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.HashMap;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * <p>
 * 用户表 服务实现类
 * </p>
 *
 * @author wabbybabbo
 * @since 2024-04-07
 */
@Slf4j
@RequiredArgsConstructor
@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements IUserService {

    private final CommonClient commonClient;
    private final UserMapper userMapper;
    // 电子邮箱验证正则表达式
    private static final String EMAIL_PATTERN = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$";
    // 编译正则表达式模式
    private static final Pattern PATTERN = Pattern.compile(EMAIL_PATTERN);

    /**
     * 验证电子邮件地址是否符合规范
     *
     * @param email 要验证的电子邮件地址
     * @return 如果电子邮件地址有效，则返回true；否则返回false
     */
    private boolean isValidEmail(String email) {
        Matcher matcher = PATTERN.matcher(email);
        return matcher.matches();
    }

    @Override
    public UserLoginVO login(UserLoginDTO userLoginDTO) {
        String name = userLoginDTO.getName();
        String password = userLoginDTO.getPassword();
        // 查询用户是否存在
        QueryWrapper<User> queryWrapper1 = new QueryWrapper<User>()
                .eq("name", name);
        if (!userMapper.exists(queryWrapper1)) {
            throw new NotFoundException(MessageConstant.ACCOUNT_NOT_FOUND);
        }
        // 查询密码是否正确
        QueryWrapper<User> queryWrapper2 = new QueryWrapper<User>()
                .eq("name", name)
                .eq("password", password);
        User user = userMapper.selectOne(queryWrapper2);
        if (user == null) {
            throw new NotFoundException(MessageConstant.PASSWORD_ERROR);
        }
        // 判断是否为管理员
        if (!user.getRole().equals(UserRoleConstant.ADMIN)) {
            log.info("[log] 不是管理员账号，无权访问");
            throw new CheckException(MessageConstant.NO_ACCESS);
        }
        // 判断该用户账号是否被禁用
        if (user.getStatus().equals(UserStatusConstant.DISABLE)) {
            log.info("[log] 该用户账号被禁用");
            throw new CheckException(MessageConstant.ACCOUNT_LOCKED);
        }

        // 构建用户信息载荷
        HashMap<String, Object> userInfo = new HashMap<>();
        userInfo.put(UserInfoConstant.USER_ID, user.getId());
        userInfo.put(UserInfoConstant.USER_ROLE, user.getRole());
        log.info("[log] 用户角色 {}", userInfo.get(UserInfoConstant.USER_ROLE));
        //成功获取到登录用户信息后，远程调用服务，生成jwt令牌
        String token = commonClient.createToken(userInfo);

        // 构建UserLoginVO
        UserLoginVO userLoginVO = new UserLoginVO();
        BeanUtils.copyProperties(user, userLoginVO);
        userLoginVO.setToken(token);

        return userLoginVO;
    }

    @Override
    public PageResult<User> pageQuery(PageQuery pageQuery) {
        // 构建分页查询条件
        Page<User> page = pageQuery.toMpPage();
        QueryWrapper<User> queryWrapper = new QueryWrapper<User>();
        List<String> filterConditions = pageQuery.getFilterConditions();
        log.info("[log] filterConditions: {}", filterConditions);
        if (null != filterConditions && !filterConditions.isEmpty()) {
            for (String condition : filterConditions) {
                if (condition.contains("=")) {
                    log.info("[log] = condition: {}", condition);
                    String[] pair = condition.split("=");
                    if (pair.length == 2)
                        queryWrapper.eq(pair[0], pair[1]);
                } else if (condition.contains("~")) {
                    log.info("[log] ~ condition: {}", condition);
                    String[] pair = condition.split("~");
                    if (pair.length == 2)
                        queryWrapper.like(pair[0], pair[1]);
                }
            }
        }
        // 分页查询
        try {
            userMapper.selectPage(page, queryWrapper);
        } catch (BadSqlGrammarException e) {
            log.error("[log] BadSqlGrammarException: {}", e.getMessage());
            throw new NotFoundException(MessageConstant.FIELD_NOT_FOUND);
        }

        return PageResult.<User>builder()
                .total(page.getTotal())
                .pages(page.getPages())
                .records(page.getRecords())
                .build();
    }

    @Override
    public void createUser(MultipartFile file, CreateUserDTO createUserDTO) {
        String role = createUserDTO.getRole();
        String gender = createUserDTO.getGender();
        String phone = createUserDTO.getPhone();
        String email = createUserDTO.getEmail();
        // 检查参数是否合法
        if (null != role) {
            if (!(role.equals(UserRoleConstant.ADMIN) || role.equals(UserRoleConstant.USER))) {
                throw new CheckException(MessageConstant.INVALID_ROLE);
            }
        }
        if (null != gender) {
            if (!(gender.equals(UserGenderConstant.MALE) || gender.equals(UserGenderConstant.FEMALE))) {
                throw new CheckException(MessageConstant.INVALID_GENDER);
            }
        }
        if (null != phone && phone.length() != 11) {
            log.info("[log] CheckException: {}", MessageConstant.INVALID_PHONE);
            throw new CheckException(MessageConstant.INVALID_PHONE);
        }
        if (null != email && !isValidEmail(email)) {
            log.info("[log] CheckException: {}", MessageConstant.INVALID_PHONE);
            throw new CheckException(MessageConstant.INVALID_EMAIL);
        }
        // 查询用户名是否被占用
        QueryWrapper<User> queryWrapper1 = new QueryWrapper<User>()
                .eq("name", createUserDTO.getName());
        if (userMapper.exists(queryWrapper1)) {
            throw new AlreadyExistsException(MessageConstant.USERNAME_ALREADY_EXISTS);
        }
        // 查询电话号码是否被占用
        QueryWrapper<User> queryWrapper2 = new QueryWrapper<User>()
                .eq("phone", phone);
        if (userMapper.exists(queryWrapper2)) {
            throw new AlreadyExistsException(MessageConstant.PHONE_ALREADY_EXISTS);
        }
        // 查询电子邮箱是否被占用
        QueryWrapper<User> queryWrapper3 = new QueryWrapper<User>()
                .eq("email", email);
        if (userMapper.exists(queryWrapper3)) {
            throw new AlreadyExistsException(MessageConstant.EMAIL_ALREADY_EXISTS);
        }
        // 构建用户对象
        User user = new User();
        BeanUtils.copyProperties(createUserDTO, user);
        if (null != file) {
            // 上传图书封面图片文件
            String url = commonClient.upload(file);
            user.setImgUrl(url);
        }
        // 新增用户
        userMapper.insert(user);
    }

    @Override
    public void disableUser(Integer id) {
        // 禁用用户账号
        User user = new User();
        user.setId(id);
        user.setStatus(UserStatusConstant.DISABLE);
        userMapper.updateById(user);
    }

    @Override
    public void enableUser(Integer id) {
        // 解禁用户账号
        User user = new User();
        user.setId(id);
        user.setStatus(UserStatusConstant.ENABLE);
        userMapper.updateById(user);
    }

    @Override
    public void batchDisableUser(List<Integer> ids) {
        // 批量禁用用户账号
        UpdateWrapper<User> updateWrapper = new UpdateWrapper<User>()
                .set("status", UserStatusConstant.DISABLE)
                .in("id", ids);
        userMapper.update(updateWrapper);
    }

    @Override
    public void batchEnableUser(List<Integer> ids) {
        // 批量解禁用户账号
        UpdateWrapper<User> updateWrapper = new UpdateWrapper<User>()
                .set("status", UserStatusConstant.ENABLE)
                .in("id", ids);
        userMapper.update(updateWrapper);
    }

}
