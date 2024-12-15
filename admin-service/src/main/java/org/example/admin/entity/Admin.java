package org.example.admin.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * <p>
 * 管理员信息表
 * </p>
 *
 * @author zhengjunpeng
 * @since 2024-12-14
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("admin")
@Schema(description="管理员信息表")
public class Admin implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    @Schema(description = "管理员ID")
    @TableId(value = "id", type = IdType.ASSIGN_ID)
    private Long id;

    @Schema(description = "管理员名称")
    @TableField("name")
    private String name;

    @Schema(description = "账号")
    @TableField("account")
    private String account;

    @Schema(description = "密码")
    @TableField("password")
    private String password;

    @Schema(description = "头像图片文件路径")
    @TableField("img_url")
    private String imgUrl;

    @Schema(description = "电话号码")
    @TableField("phone")
    private String phone;

    @Schema(description = "邮箱")
    @TableField("email")
    private String email;

    @Schema(description = "联系地址")
    @TableField("address")
    private String address;

    @Schema(description = "账号状态")
    @TableField("status")
    private String status;

    @Schema(description = "创建时间")
    @TableField("create_time")
    private LocalDateTime createTime;

    @Schema(description = "更新时间")
    @TableField("update_time")
    private LocalDateTime updateTime;


}
