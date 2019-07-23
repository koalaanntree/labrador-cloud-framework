package net.bestjoy.cloud.core.reader;

import com.alibaba.fastjson.JSONObject;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import net.bestjoy.cloud.core.reader.enums.ExcelVersionEnum;
import net.bestjoy.cloud.core.reader.template.SimpleDataTemplate;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/***
 * 简单读取excel文件里的数据
 *
 * @param <T> 转换的对象
 *
 * @author ray
 */
@Slf4j
@RequiredArgsConstructor
public class SimpleDataExcelReader<T> extends ExcelReader implements DataReader<T> {
    /***
     * 数据模板
     */
    @NonNull
    private SimpleDataTemplate<T> simpleDataTemplate;

    public SimpleDataExcelReader(SimpleDataTemplate simpleDataTemplate, InputStream inputStream, ExcelVersionEnum excelVersion) {
        this(simpleDataTemplate);
        this.inputStream = inputStream;
        this.excelVersion = excelVersion;
    }

    @Override
    @SneakyThrows
    public List<Map<String, Object>> resolve() {
        Workbook workbook = getWorkbook();

        Sheet sheet = loadSheet(workbook, 0);

        int startRow = simpleDataTemplate.getStartRow();
        int startColumn = simpleDataTemplate.getStartColumn();

        List<Map<String, Object>> list = new ArrayList<>();

        for (int i = startRow; i <= sheet.getLastRowNum(); i++) {
            Row row = sheet.getRow(i);
            Map<String, Object> map = new HashMap<>();
            for (int j = startColumn; j < (simpleDataTemplate.getDataDefines().size() + startColumn); j++) {
                Cell cell = row.getCell(j);

                Object value = parseCellValue(cell);

                map.put(simpleDataTemplate.getDataDefines().get(j - startColumn).getName(), value);
            }

            list.add(map);
        }

        return list;
    }

    @Override
    public List<T> resolveToBean(Class<T> clazz) {
        List<Map<String, Object>> list = resolve();

        if (CollectionUtils.isEmpty(list)) {
            return new ArrayList<>();
        }

        return JSONObject.parseArray(JSONObject.toJSONString(list), clazz);
    }
}
