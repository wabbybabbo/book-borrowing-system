package org.example.client.controller;


import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.client.pojo.dto.UpdateUserDTO;
import org.example.client.pojo.dto.UserLoginDTO;
import org.example.client.pojo.dto.UserRegisterDTO;
import org.example.client.pojo.vo.UserVO;
import org.example.client.service.IUserService;
import org.example.common.constant.MessageConstant;
import org.example.common.result.Result;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

/**
 * <p>
 * 用户表 前端控制器
 * </p>
 *
 * @author wabbybabbo
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

    @Operation(summary = "用户登录")
    @GetMapping("/login")
    public Result<UserVO> login(@ParameterObject @Valid UserLoginDTO userLoginDTO) {
        log.info("[log] 用户登录 userLoginDTO: {}", userLoginDTO);

        //查询登录用户信息
        UserVO userVO = userService.login(userLoginDTO);

        return Result.success(MessageConstant.LOGIN_SUCCESS, userVO);
    }

    @Operation(summary = "用户注册")
    @PostMapping("/register")
    public Result register(@RequestBody @Valid UserRegisterDTO userRegisterDTO /*@RequestBody用于接收请求体的内容(JSON)，并将其转换为Java对象绑定到方法的参数上。*/) {
        log.info("[log] 用户注册 userRegisterDTO: {}", userRegisterDTO);
        userService.register(userRegisterDTO);
        return Result.success(MessageConstant.REGISTER_SUCCESS);
    }

    @Operation(summary = "更改用户信息")
    @PutMapping
    public Result updateUser(@RequestBody @Valid UpdateUserDTO updateUserDTO) {
        log.info("[log] 更改用户信息 updateUserDTO: {}", updateUserDTO);
        userService.updateUser(updateUserDTO);
        return Result.success(MessageConstant.UPDATE_SUCCESS);
    }

}
