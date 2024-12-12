package org.example.client.pojo.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@Schema(description = "更改用户信息时传递的数据模型")
public class UpdateUserDTO {

    @Schema(description = "用户名")
    private String name;

    @Schema(description = "密码")
    private String password;

    @Schema(description = "用户头像图片文件路径")
    private String imgUrl;

    @Schema(description = "性别")
    private String gender;

    @Schema(description = "电话号码")
    private String phone;

    @Schema(description = "邮箱")
    private String email;

    @Schema(description = "地址")
    private String address;

}
