package org.example.admin.pojo.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serializable;

@Data
@Schema(description = "查询书籍类别时响应的数据模型")
public class CategoryVO implements Serializable {

    @Schema(description = "书籍类别ID")
    private String id;

    @Schema(description = "类别名称")
    private String name;

}
