package net.bestjoy.cloud.core.bean;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.ArrayList;
import java.util.List;

/***
 * 分页对象
 * @param <T>
 * @author ray
 */
@Data
@ToString
@NoArgsConstructor
public class PageData<T> {

    /***
     * 数据
     */
    private List<T> records;

    /***
     * 分页信息
     */
    private PageInfo pageInfo;

    public static PageData<?> builderPageData(List<?> list, PageInfo pageInfo) {
        PageData pageData = new PageData();
        pageData.setRecords(list);
        pageData.setPageInfo(pageInfo);
        return pageData;
    }

    /**
     * 返回空
     *
     * @param current
     * @param size
     * @param total
     * @return
     */
    public static PageData emptyResult(long current, long size, long total) {

        return builderPageData(new ArrayList(), PageInfo.builder().current(current).total(total).size(size).build());
    }

    /***
     * 返回空
     * @return
     */
    public static PageData emptyResult() {
        return emptyResult(0L, 0L, 0L);
    }

    public static PageData buildResult(List records, long current, long size, long total) {
        return builderPageData(records, PageInfo.builder().current(current).total(total).size(size).build());
    }

    public static PageData buildResult(Page page) {
        return page == null ? emptyResult() : buildResult(page.getRecords(), page.getCurrent(), page.getSize(), page.getTotal());
    }

    public static PageData buildResult(IPage iPage) {
        return iPage == null ? emptyResult() : buildResult(iPage.getRecords(), iPage.getCurrent(), iPage.getSize(), iPage.getTotal());
    }
}
