package org.example.admin.pojo.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serializable;

/**
 * 查询出版社信息时响应的数据模型
 */
@Data
@Schema(description = "查询出版社信息时响应的数据模型")
public class PublisherVO implements Serializable {

    @Schema(description = "出版社ID")
    private String id;

    @Schema(description = "出版社名称")
    private String name;

}
