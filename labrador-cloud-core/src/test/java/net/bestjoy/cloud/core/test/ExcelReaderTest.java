package net.bestjoy.cloud.core.test;

import com.alibaba.fastjson.JSONObject;
import lombok.SneakyThrows;
import net.bestjoy.cloud.core.reader.SimpleDataExcelReader;
import net.bestjoy.cloud.core.reader.enums.ExcelVersionEnum;
import net.bestjoy.cloud.core.reader.template.SimpleDataTemplate;
import net.bestjoy.cloud.core.test.bean.TestDto;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.io.File;
import java.io.FileInputStream;
import java.util.List;

/**
 * @author ray
 */
@RunWith(SpringJUnit4ClassRunner.class)
public class ExcelReaderTest {

    @Test
    @SneakyThrows
    public void test() {
        FileInputStream fileInputStream = new FileInputStream(new File("D:\\test.xlsx"));

        SimpleDataTemplate<TestDto> simpleTemplate = new SimpleDataTemplate<>(TestDto.class);
        simpleTemplate.setStartRow(1);
        simpleTemplate.setStartColumn(1);

        SimpleDataExcelReader<TestDto> simpleExcelReader =
                new SimpleDataExcelReader<>(simpleTemplate, fileInputStream, ExcelVersionEnum.OFFICE_2007U);

        List<TestDto> list = simpleExcelReader.resolveToBean(TestDto.class);

        System.out.println(JSONObject.toJSONString(list));
    }
}
