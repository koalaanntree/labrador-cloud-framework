package net.bestjoy.cloud.cache.jsr107.nio;

import java.util.concurrent.atomic.DoubleAdder;
import java.util.concurrent.atomic.LongAccumulator;
import java.util.concurrent.atomic.LongAdder;
import java.util.function.IntConsumer;

/**
 * 缓冲区分配预测器（基于统计）
 * [说明]:
 * <p>
 * 我们知道，如果ByteBuf分配的初始容量小于写入的字节码数的话，是会进行自动扩容的.
 * 那么问题来了，如果我每次分配的ByteBuf容量小于实际所需要的（主要是读取）容量，
 * 那么就会频繁扩容，涉及到内存空间重新分配和深拷贝，代价比较大，
 * 所以说每次读取之前准备好足够大小的ByteBuf也是能提高性能的。
 * <p>
 * 但是我们也不能浪费空间，所以准备了本类
 * 其作用就是根据用户实际使用情况，合理预测缓存区大小
 */
public class BufferAllocationPredictor implements IntConsumer {

    private final LongAdder count = new LongAdder();
    private final DoubleAdder sum = new DoubleAdder();
    private final LongAccumulator min = new LongAccumulator(Long::min, Long.MAX_VALUE);
    private final LongAccumulator max = new LongAccumulator(Long::max, Long.MIN_VALUE);
    private final int initial;
    private final int threshold;

    /**
     * 默认ByteBuf的大小为
     * 最小 128bit = 16B
     * 最大 256bit = 32B
     */
    public BufferAllocationPredictor() {
        this(128, 256);
    }

    public BufferAllocationPredictor(int initial, int threshold) {
        this.initial = initial;
        this.threshold = threshold;
    }

    /**
     * 每次设定分配空间后做如下四件事：
     * 1. 分配次数 + 1
     * 2. 记录累加分配内存大小
     * 3. min 比较上一次value，将最大值保留
     * 4. max 比较上一次value，将最大值保留
     * <p>
     * 1,2两步，为了计算得出最大值
     * 3,4两步，为了比对上次做的预测成功还是失败
     */
    @Override
    public void accept(int value) {
        if (value < 0) {
            throw new IllegalArgumentException();
        }
        count.increment();
        sum.add(value);
        min.accumulate(value);
        max.accumulate(value);
    }

    public long getCount() {
        return count.longValue();
    }

    public double getSum() {
        return sum.doubleValue();
    }

    public int getMin() {
        int value = min.intValue();
        return value == Integer.MAX_VALUE ? 0 : value;
    }

    public int getMax() {
        int value = max.intValue();
        return value == Integer.MIN_VALUE ? 0 : value;
    }

    public int getPredicted() {
        int max = getMax();
        if (max == 0) {
            return initial;
        }
        if (max < threshold) {
            return max;
        }
        long count = getCount();
        double sum = getSum();
        double avg = sum / count;

        if (max > avg * 2) {
            return (int) avg;
        }
        return (int) (avg + max) * 3 / 4;
    }
}
