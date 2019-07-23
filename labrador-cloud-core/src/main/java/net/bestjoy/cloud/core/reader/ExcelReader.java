package net.bestjoy.cloud.core.reader;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import net.bestjoy.cloud.core.reader.enums.ExcelVersionEnum;
import net.bestjoy.cloud.core.util.Dates;
import net.bestjoy.cloud.error.bean.BusinessException;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.text.DecimalFormat;

import static net.bestjoy.cloud.error.bean.Errors.Biz.EXCEL_RESOLVE_ERROR;

/***
 * excel reader
 * @author ray
 */
@Data
@NoArgsConstructor
public class ExcelReader {
    /***
     * excel文件流
     */
    protected InputStream inputStream;

    /**
     * excel版本
     */
    protected ExcelVersionEnum excelVersion;
    
    /**
     * 解析workbook
     *
     * @return
     * @throws IOException
     */
    protected Workbook getWorkbook() throws IOException {

        Workbook workbook = null;
        if (ExcelVersionEnum.OFFICE_2007U.equals(this.excelVersion)) {
            workbook = new XSSFWorkbook(this.inputStream);
        }

        if (ExcelVersionEnum.OFFICE_2003L.equals(this.excelVersion)) {
            workbook = new HSSFWorkbook(this.inputStream);
        }

        if (workbook == null) {
            throw new BusinessException(EXCEL_RESOLVE_ERROR);
        }

        return workbook;
    }

    /***
     * 加载sheet
     * @param workbook
     * @param sheetIndex
     * @return
     */
    protected Sheet loadSheet(Workbook workbook, Integer sheetIndex) {
        if (workbook == null) {
            return null;
        }

        return workbook.getSheetAt(sheetIndex);
    }

    /**
     * 读取单元格
     *
     * @param cell
     * @return
     */
    protected Object parseCellValue(Cell cell) {
        if (cell == null) {
            return null;
        }
        switch (cell.getCellTypeEnum()) {
            case STRING:
                return cell.getRichStringCellValue().getString();
            case BLANK:
                return "";
            case ERROR:
            case _NONE:
                return null;
            case BOOLEAN:
                return cell.getBooleanCellValue();
            case NUMERIC:
                if ("General".equals(cell.getCellStyle().getDataFormatString())) {
                    DecimalFormat decimalFormat = new DecimalFormat("0");
                    return decimalFormat.format(cell.getNumericCellValue());
                } else if ("m/d/yy".equals(cell.getCellStyle().getDataFormatString())) {
                    return Dates.format(cell.getDateCellValue(), "yyyy-MM-dd");
                } else {
                    return new BigDecimal(cell.getNumericCellValue());
                }
            default:
                return null;
        }
    }
}
