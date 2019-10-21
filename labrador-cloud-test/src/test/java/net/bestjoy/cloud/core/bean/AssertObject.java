package net.bestjoy.cloud.core.bean;

import lombok.Data;
import lombok.ToString;
import net.bestjoy.cloud.core.annotation.NotNull;

/***
 * @author ray
 */
@Data
@ToString
public class AssertObject {

    @NotNull
    private String name;

    private String value;
}
