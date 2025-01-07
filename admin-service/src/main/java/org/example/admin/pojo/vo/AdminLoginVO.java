package org.example.admin.pojo.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * 管理员登录时响应的数据模型
 */
@Data
@Schema(description = "管理员登录时响应的数据模型")
public class AdminLoginVO {

    @Schema(description = "管理员ID")
    private String id;

    @Schema(description = "头像图片文件路径")
    private String imgUrl;

    @Schema(description = "管理员名称")
    private String name;

    @Schema(description = "账号")
    private String account;

    @Schema(description = "密码")
    private String password;

    @Schema(description = "电话号码")
    private String phone;

    @Schema(description = "邮箱")
    private String email;

    @Schema(description = "联系地址")
    private String address;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Schema(description = "账号创建时间")
    private LocalDateTime createTime;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Schema(description = "最新信息更改时间")
    private LocalDateTime updateTime;

    @Schema(description = "令牌")
    private String token;

}
