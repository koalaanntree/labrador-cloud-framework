package net.bestjoy.cloud.core.reader.enums;

import org.apache.commons.lang3.StringUtils;

/**
 * excel版本
 *
 * @author ray
 */
public enum ExcelVersionEnum {
    //2003- 版本的excel
    OFFICE_2003L,
    //2007+ 版本的excel
    OFFICE_2007U;

    /**
     * 根据文件后缀获取excel版本
     *
     * @param suffix
     * @return
     */
    public static ExcelVersionEnum convertVersionBySuffix(String suffix) {
        if (StringUtils.equalsIgnoreCase(suffix, ".xls")) {
            return OFFICE_2003L;
        }

        if (StringUtils.equalsIgnoreCase(suffix, ".xlsx")) {
            return OFFICE_2007U;
        }

        return null;
    }
}
