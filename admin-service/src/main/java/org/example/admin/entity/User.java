package org.example.admin.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * <p>
 * 用户信息表
 * </p>
 *
 * @author zhengjunpeng
 * @since 2024-04-07
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("user")
@Schema(description = "用户信息表")
public class User implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    @Schema(description = "用户ID")
    @TableId(value = "id", type = IdType.ASSIGN_ID)
    private Long id;

    @Schema(description = "用户名")
    private String name;

    @Schema(description = "账号")
    private String account;

    @Schema(description = "密码")
    private String password;

    @Schema(description = "头像图片文件路径")
    private String imgUrl;

    @Schema(description = "性别")
    private String gender;

    @Schema(description = "电话号码")
    private String phone;

    @Schema(description = "邮箱")
    private String email;

    @Schema(description = "联系地址")
    private String address;

    @Schema(description = "账号状态")
    private String status;

    @Schema(description = "创建时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:MM:SS")
    private LocalDateTime createTime;

    @Schema(description = "更新时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:MM:SS")
    private LocalDateTime updateTime;


}
