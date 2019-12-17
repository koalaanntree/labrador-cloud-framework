package net.bestjoy.cloud.cache;

import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import lombok.Getter;
import lombok.Setter;
import net.bestjoy.cloud.core.util.Dates;
import net.bestjoy.cloud.error.bean.SysException;
import org.springframework.data.redis.core.RedisTemplate;

import java.math.BigDecimal;
import java.util.Date;
import java.util.concurrent.TimeUnit;

/***
 * 缓存自增id工具类
 *
 * 默认除去前缀16位
 *
 * todo 完善策略
 * @author ray
 */
public abstract class AbstractCacheIncreasingIdProvider {

    /***
     *  cacheKeyPre like xxx:SelfIncreasingId:2019121616
     */
    private final static String INCREASING_ID_PRE = ":SelfIncreasingId:";
    /***
     * 自增步数，默认增1
     */
    @Getter
    @Setter
    private Integer increasingStep = 1;

    /***
     * 自增key超时时间，结合timeUnit
     */
    @Getter
    @Setter
    private Integer increasingCacheExpireTime = 1;

    @Getter
    @Setter
    private TimeUnit timeUnit = TimeUnit.MINUTES;

    /***
     * 自增位数，默认4位
     */
    @Setter
    @Getter
    private int increasingDigit = 4;

    /***
     * 生成时的时间
     */
    @Getter
    private Date generateTime;

    /***
     * 中间时间戳字符串
     */
    private String generateDateStr;

    /***
     * 支持的对象
     * @return
     */
    public abstract Class<?> entityClazzSupport();

    /***
     * id前缀
     * @return
     */
    public abstract String increasingIdPrefix();

    /***
     * 获取缓存实例，如果不存在，返回null
     * @return
     */
    public abstract RedisTemplate getRedisTemplate();

    /***
     * 自增缓存key前缀
     * @return
     */
    private String getIncreasingKeyWithTime() {
        if (timeUnit == null) {
            throw new SysException("010003", "self increasing time unit invalid.");
        }
        generateTime = new Date();
        switch (timeUnit) {
            case DAYS:
                generateDateStr = Dates.format(generateTime, "yyyyMMdd");
                break;
            case HOURS:
                generateDateStr = Dates.format(generateTime, "yyyyMMddHH");
                break;
            case MINUTES:
                generateDateStr = Dates.format(generateTime, "yyyyMMddHHmm");
                break;
            case SECONDS:
                generateDateStr = Dates.format(generateTime, "yyyyMMddHHmmSS");
                break;
            default:
                throw new SysException("010000", "self increasing cache time unit not support.");
        }

        return entityClazzSupport().toGenericString() + INCREASING_ID_PRE + generateDateStr;
    }

    /***
     * 获取缓存自增id，目前支持时间戳方式生成nextId
     * @return
     */
    public String generateCacheIncreasingId() {
        if (getRedisTemplate() == null) {
            throw new SysException("010002", "self increasing redis template not provider");
        }

        if (increasingStep <= 0) {
            throw new SysException("010001", "self increasing step invalid.");
        }

        String cacheKey = getIncreasingKeyWithTime();
        long nextId = getIncreasingDigitNum() + getRedisTemplate().opsForValue().increment(cacheKey, increasingStep);
        getRedisTemplate().expire(cacheKey, increasingCacheExpireTime, timeUnit);

        StringBuffer stringBuffer = new StringBuffer();
        stringBuffer.append(StringUtils.isBlank(increasingIdPrefix()) ? "" : increasingIdPrefix())
                .append(generateDateStr)
                .append(nextId);
        //todo 中间位数扩展
        return stringBuffer.toString();
    }


    private int getIncreasingDigitNum() {
        if (increasingDigit <= 0) {
            throw new SysException("010001", "self increasing digit invalid.");
        }

        return new BigDecimal(1).movePointRight(4).intValue();
    }

}
