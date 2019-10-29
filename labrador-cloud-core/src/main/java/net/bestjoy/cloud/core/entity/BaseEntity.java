package net.bestjoy.cloud.core.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;
import lombok.ToString;
import lombok.experimental.Accessors;

import java.util.Date;

/***
 * @author ray
 *
 */
@Data
@ToString
@Accessors(chain = true)
public class BaseEntity<PK> {
    /***
     * 主键id
     */
    @TableId(type = IdType.AUTO)
    private PK id;

    /***
     * 创建时间
     */
    private Date createTime;


    /**
     * 最后更新时间
     */
    private Date updateTime;
}
