package net.bestjoy.cloud.core.test;

import net.bestjoy.cloud.core.bean.SubResult;
import net.bestjoy.cloud.error.bean.ErrorCodeAndMessage;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/***
 * @author ray
 */
@RunWith(SpringJUnit4ClassRunner.class)
public class ResultTest {

    @Test
    public void testSubResult() {
        SubResult result = SubResult.fail(ErrorCodeAndMessage.create("9999", "错误"), "99991", "字错误");

        System.out.println(result.toString());
    }

}
