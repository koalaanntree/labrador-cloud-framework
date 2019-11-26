package net.bestjoy.cloud.core.bean;

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
    private Integer page = 1;

    /***
     * 每页显示个数
     */
    private Integer limit = 20;
}
