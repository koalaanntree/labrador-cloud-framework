package net.bestjoy.cloud.core.bean;

import lombok.Builder;
import lombok.Data;
import lombok.ToString;

/**
 * 分页信息
 *
 * @author ray
 */
@Data
@ToString
@Builder
public class PageInfo {
    /***
     * 总数
     */
    private long total;
    /***
     * 总页数
     */
    private long size;
    /***
     * 当前页数
     */
    private long current;
}
