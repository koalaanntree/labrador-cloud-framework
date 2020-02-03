package net.bestjoy.cloud.core.test;

import net.bestjoy.cloud.core.bean.SubResult;
import net.bestjoy.cloud.core.test.bean.TestDto;
import net.bestjoy.cloud.error.bean.ErrorCodeAndMessage;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/***
 * @author ray
 */
@RunWith(SpringJUnit4ClassRunner.class)
@EnableAspectJAutoProxy
public class ResultTest {

    @Test
    public void testSubResult() {

        TestDto testDto= new TestDto();

        System.out.println(testDto.toString());

    }

}
