package org.example.client.pojo.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serializable;

@Data
@Schema(description = "查询图书类别时响应的数据模型")
public class CategoryVO implements Serializable {

    @Schema(description = "图书类别ID")
    private Integer id;

    @Schema(description = "类别名称")
    private String name;

}
