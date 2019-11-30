package net.bestjoy.cloud.core.bean;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.ToString;

import java.io.Serializable;

/**
 * 分页请求
 *
 * @author ray
 */
@Data
@ToString
public class PageBean implements Serializable {
    /**
     * 起始页
     */
    @ApiModelProperty("页")
    private Integer page = 1;

    /***
     * 每页显示个数
     */
    @ApiModelProperty(value = "每页显示个数", notes = "默认20")
    private Integer limit = 20;
}
