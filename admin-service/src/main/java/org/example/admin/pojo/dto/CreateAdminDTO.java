package org.example.admin.pojo.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import org.example.common.constant.MessageConstant;
import org.hibernate.validator.constraints.Length;

@Data
@Schema(description = "新建管理员账号时传递的数据模型", requiredProperties = {"name", "account", "password"})
public class CreateAdminDTO {

    @Schema(description = "管理员名称", minLength = 2, maxLength = 8)
    @Length(min = 2, max = 8, message = MessageConstant.INVALID_USER_NAME)
    private String name;

    @Schema(description = "账号", minLength = 4, maxLength = 16)
    @Length(min = 4, max = 16, message = MessageConstant.INVALID_ACCOUNT)
    private String account;

    @Schema(description = "密码", minLength = 4, maxLength = 16)
    @Length(min = 4, max = 16, message = MessageConstant.INVALID_PASSWORD)
    private String password;

    @Schema(description = "头像图片文件路径")
    private String imgUrl;

    @Schema(description = "电话号码", minLength = 11, maxLength = 11)
    private String phone;

    @Schema(description = "邮箱")
    private String email;

    @Schema(description = "联系地址")
    private String address;

}
