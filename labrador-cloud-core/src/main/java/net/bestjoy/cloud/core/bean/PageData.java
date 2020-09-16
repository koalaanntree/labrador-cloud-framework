package net.bestjoy.cloud.core.bean;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import net.bestjoy.cloud.core.converter.BeanConverter;
import org.springframework.util.CollectionUtils;

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

    /**
     * 构造分页返回结果
     *
     * @param page
     * @param beanConverter
     * @param <T>
     * @param <O>
     * @return
     */
    public static <T, O> PageData<T> buildResult(IPage<O> page, BeanConverter<T, O> beanConverter) {

        if (page == null || CollectionUtils.isEmpty(page.getRecords())) {
            return emptyResult();
        }

        List<T> result = new ArrayList<>();
        page.getRecords().forEach(item -> {
            result.add(beanConverter.convert(item));
        });
        return buildResult(result, page.getCurrent(), page.getSize(), page.getTotal());
    }


    public static <T> PageData<T> builderPageData(List<T> list, PageInfo pageInfo) {
        PageData<T> pageData = new PageData<>();
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
    public static <T> PageData<T> emptyResult(long current, long size, long total) {

        return builderPageData(new ArrayList<>(), PageInfo.builder().current(current).total(total).size(size).build());
    }

    /***
     * 返回空
     * @return
     */
    public static <T> PageData<T> emptyResult() {
        return emptyResult(0L, 0L, 0L);
    }

    public static <T> PageData<T> buildResult(List<T> records, long current, long size, long total) {
        return builderPageData(records, PageInfo.builder().current(current).total(total).size(size).build());
    }

    public static <T> PageData<T> buildResult(Page<T> page) {
        return page == null ? emptyResult() : buildResult(page.getRecords(), page.getCurrent(), page.getSize(), page.getTotal());
    }

    public static <T> PageData<T> buildResult(IPage<T> iPage) {
        return iPage == null ? emptyResult() : buildResult(iPage.getRecords(), iPage.getCurrent(), iPage.getSize(), iPage.getTotal());
    }

    public static <T> Result<PageData<T>> buildResultResponse(IPage<T> page) {
        return Result.success(buildResult(page));
    }
}
