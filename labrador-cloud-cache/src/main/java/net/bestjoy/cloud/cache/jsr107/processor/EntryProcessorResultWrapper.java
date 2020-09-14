package net.bestjoy.cloud.cache.jsr107.processor;

import javax.cache.CacheException;
import javax.cache.processor.EntryProcessorException;
import javax.cache.processor.EntryProcessorResult;
import java.util.Objects;

/**
 * TODO:.....
 */
public class EntryProcessorResultWrapper<T> implements EntryProcessorResult<T> {
    protected final T value;
    protected final CacheException exception;

    public EntryProcessorResultWrapper(T value) {
        this.value = value;
        this.exception = null;
    }

    public EntryProcessorResultWrapper(CacheException exception) {
        this.value = null;
        this.exception = exception;
    }

    @Override
    public T get() throws EntryProcessorException {
        if (exception != null) {
            throw exception;
        }
        return value;
    }

    @Override
    public String toString() {
        return Objects.toString(get());
    }
}
