package org.example.client.controller;


import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.client.pojo.dto.UpdateUserDTO;
import org.example.client.pojo.dto.UserLoginDTO;
import org.example.client.pojo.dto.UserRegisterDTO;
import org.example.client.pojo.vo.UserVO;
import org.example.client.service.IUserService;
import org.example.common.constant.ClaimConstant;
import org.example.common.constant.MessageConstant;
import org.example.common.result.Result;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

/**
 * <p>
 * 用户信息表 前端控制器
 * </p>
 *
 * @author zhengjunpeng
 * @since 2024-04-07
 */
@Slf4j
@Validated
@RequiredArgsConstructor
@RestController
@RequestMapping("/user")
@Tag(name = "用户相关接口")
public class UserController {

    private final IUserService userService;

    @GetMapping(value = "/captcha")
    @Operation(summary = "获取动态图形验证码")
    public void getGifCaptcha(
            @Parameter(description = "时间戳", required = true)
            String timestamp,
            HttpServletResponse response) {
        log.info("[log] 开始获取动态图形验证码");
        String code = userService.createGifCaptcha(timestamp, response);
        log.info("[log] 生成的验证码为：{}", code);
    }

    @GetMapping("/login")
    @Operation(summary = "用户登录")
    public Result<UserVO> login(@ParameterObject @Valid UserLoginDTO userLoginDTO) {
        log.info("[log] 用户登录 {}", userLoginDTO);
        String code = userService.getCodeCache(userLoginDTO.getTimestamp());
        UserVO userVO = userService.login(userLoginDTO, code);
        return Result.success(MessageConstant.LOGIN_SUCCESS, userVO);
    }

    @PostMapping("/register")
    @Operation(summary = "用户注册")
    public Result<Object> register(@RequestBody @Valid UserRegisterDTO userRegisterDTO /*@RequestBody用于接收请求体的内容(JSON)，并将其转换为Java对象绑定到方法的参数上。*/) {
        log.info("[log] 用户注册 {}", userRegisterDTO);
        userService.register(userRegisterDTO);
        return Result.success(MessageConstant.REGISTER_SUCCESS);
    }

    @PutMapping
    @Operation(summary = "更改用户信息")
    public Result<Object> updateUser(
            @RequestBody @Valid
            UpdateUserDTO updateUserDTO,
            @RequestHeader(ClaimConstant.CLIENT_ID)
            @Parameter(description = "用户ID", required = true)
            String id) {
        log.info("[log] 更改用户信息 {}", updateUserDTO);
        userService.updateUser(updateUserDTO, id);
        return Result.success(MessageConstant.UPDATE_SUCCESS);
    }

}
