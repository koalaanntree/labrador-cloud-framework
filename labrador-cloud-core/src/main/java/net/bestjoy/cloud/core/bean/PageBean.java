package net.bestjoy.cloud.core.bean;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
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
public class PageBean<T> implements Serializable {
    /**
     * 起始页
     */
    @ApiModelProperty("页")
    private Integer pageNum = 1;

    /***
     * 每页显示个数
     */
    @ApiModelProperty(value = "每页显示个数", notes = "默认20")
    private Integer limit = 20;

    public Page<T> getPage() {
        return new Page<>(pageNum, limit);
    }
}
