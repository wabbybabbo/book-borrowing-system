package org.example.client.controller;


import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.RequiredArgsConstructor;
import org.example.client.pojo.dto.UpdateEmailDTO;
import org.example.client.pojo.dto.UpdateUserDTO;
import org.example.client.pojo.dto.UserLoginDTO;
import org.example.client.pojo.dto.UserRegisterDTO;
import org.example.client.pojo.vo.UserVO;
import org.example.client.service.IUserService;
import org.example.common.constant.ClaimConstant;
import org.example.common.constant.MessageConstant;
import org.example.common.constant.RegexpConstant;
import org.example.common.result.Result;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.http.MediaType;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

/**
 * <p>
 * 用户信息表 前端控制器
 * </p>
 *
 * @author zhengjunpeng
 * @since 2024-04-07
 */
@Validated
@RequiredArgsConstructor
@RestController
@RequestMapping("/user")
@Tag(name = "用户相关接口")
public class UserController {

    private final IUserService userService;

    @GetMapping(value = "/register/captcha")
    @Operation(summary = "发送验证码到邮箱用于用户注册，并设置验证码有效时长")
    public Result<String> sendCaptcha2Email4Register(
            @Email(regexp = RegexpConstant.EMAIL, message = MessageConstant.INVALID_EMAIL)
            @Parameter(description = "邮箱", required = true)
            String email,
            @PositiveOrZero(message = MessageConstant.INVALID_CAPTCHA_TIMEOUT)
            @Parameter(description = "验证码有效时长（分钟）", required = true)
            Long timeout) {
        userService.sendCaptcha2Email4Register(email, timeout);
        return Result.success(MessageConstant.SEND_CAPTCHA_SUCCESS);
    }

    @GetMapping(value = "/email/captcha")
    @Operation(summary = "发送验证码到邮箱用于换绑邮箱，并设置验证码有效时长")
    public Result<String> sendCaptcha2Email4UpdateEmail(
            @Email(regexp = RegexpConstant.EMAIL, message = MessageConstant.INVALID_EMAIL)
            @Parameter(description = "邮箱", required = true)
            String email,
            @PositiveOrZero(message = MessageConstant.INVALID_CAPTCHA_TIMEOUT)
            @Parameter(description = "验证码有效时长（分钟）", required = true)
            Long timeout) {
        userService.sendCaptcha2Email4UpdateEmail(email, timeout);
        return Result.success(MessageConstant.SEND_CAPTCHA_SUCCESS);
    }

    @PostMapping("/register")
    @Operation(summary = "用户注册")
    public Result<Object> register(@RequestBody @Valid UserRegisterDTO userRegisterDTO /*@RequestBody用于接收请求体的内容(JSON)，并将其转换为Java对象绑定到方法的参数上。*/) {
        userService.register(userRegisterDTO);
        return Result.success(MessageConstant.REGISTER_SUCCESS);
    }

    @PutMapping("/email")
    @Operation(summary = "换绑邮箱")
    public Result<Object> updateEmail(
            @RequestHeader(ClaimConstant.CLIENT_ID)
            @NotNull(message = MessageConstant.FIELD_NOT_NULL)
            @Parameter(description = "用户ID", required = true, hidden = true)
            String id,
            @RequestBody @Valid
            UpdateEmailDTO updateEmailDTO) {
        userService.updateEmail(id, updateEmailDTO);
        return Result.success(MessageConstant.UPDATE_EMAIL_SUCCESS);
    }

    @GetMapping(value = "/login/captcha")
    @Operation(summary = "获取动态图形验证码")
    public void getGifCaptcha(
            @Parameter(description = "时间戳", required = true)
            String timestamp,
            HttpServletResponse response) {
        userService.createGifCaptcha(timestamp, response);
    }

    @GetMapping("/login")
    @Operation(summary = "用户登录")
    public Result<UserVO> login(@ParameterObject @Valid UserLoginDTO userLoginDTO) {
        UserVO userVO = userService.login(userLoginDTO);
        return Result.success(MessageConstant.LOGIN_SUCCESS, userVO);
    }

    @PutMapping
    @Operation(summary = "更改用户信息")
    public Result<Object> updateUser(
            @RequestHeader(ClaimConstant.CLIENT_ID)
            @NotNull(message = MessageConstant.FIELD_NOT_NULL)
            @Parameter(description = "用户ID", required = true, hidden = true)
            String id,
            @RequestBody @Valid
            UpdateUserDTO updateUserDTO) {
        userService.updateUser(id, updateUserDTO);
        return Result.success(MessageConstant.UPDATE_SUCCESS);
    }

    @PutMapping(value = "/avatar", consumes = MediaType.MULTIPART_FORM_DATA_VALUE /*指定multipart/form-data*/)
    @Operation(summary = "更换用户头像")
    public Result<String> updateAvatar(
            @RequestHeader(ClaimConstant.CLIENT_ID)
            @NotNull(message = MessageConstant.FIELD_NOT_NULL)
            @Parameter(description = "用户ID", required = true, hidden = true)
            String id,
            @RequestPart("file")
            @Parameter(description = "用户头像图片文件")
            MultipartFile file) {
        String url = userService.updateAvatar(id, file);
        return Result.success(MessageConstant.UPDATE_SUCCESS, url);
    }

}
