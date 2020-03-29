package net.bestjoy.cloud.core.test;

import net.bestjoy.cloud.core.annotation.NeedValidate;
import net.bestjoy.cloud.core.config.CoreConfig;
import net.bestjoy.cloud.core.test.bean.TestDto;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/***
 * @author ray
 */
@RunWith(SpringJUnit4ClassRunner.class)
@Import(CoreConfig.class)
public class ResultTest {


    @NeedValidate
    public void testMethod(TestDto testDto) {
        System.out.println(testDto.toString());
    }

    @Test
    public void testSubResult() {

        TestDto testDto = new TestDto();

        testMethod(testDto);

    }

}
