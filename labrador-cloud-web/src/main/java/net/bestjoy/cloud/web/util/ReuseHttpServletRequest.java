package net.bestjoy.cloud.web.util;

import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.http.MediaType;

import javax.servlet.ReadListener;
import javax.servlet.ServletInputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;
import java.io.*;

/***
 * 可复用http请求
 * @author ray
 */
@Slf4j
public final class ReuseHttpServletRequest extends HttpServletRequestWrapper {

    private final HttpServletRequest request;

    private byte[] body;

    @SneakyThrows
    public ReuseHttpServletRequest(HttpServletRequest request) {
        super(request);
        this.request = request;
        this.body = toBytes(request.getInputStream());
    }

    /**
     * 获取http的请求body体
     *
     * @return
     * @throws Exception
     */
    Object getBody() throws Exception {
        if (StringUtils.containsIgnoreCase(request.getContentType(), MediaType.MULTIPART_FORM_DATA_VALUE)) {
            //upload file
            if (log.isDebugEnabled()) {
                log.info("request is multipart/form-data >");
            }
            return request.getParts();
        } else {
            if (body != null) {
                return new String(body);
            }
        }

        return StringUtils.EMPTY;
    }

    @SneakyThrows
    private byte[] toBytes(InputStream inputStream) {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();

        byte[] buff = new byte[1024];
        int n = 0;
        while ((n = inputStream.read(buff)) != -1) {
            byteArrayOutputStream.write(buff, 0, n);
        }
        return byteArrayOutputStream.toByteArray();
    }

    @Override
    public BufferedReader getReader() throws IOException {
        return new BufferedReader(new InputStreamReader(getInputStream()));
    }

    @Override
    public ServletInputStream getInputStream() throws IOException {
        final ByteArrayInputStream inputStream = new ByteArrayInputStream(body);

        return new ServletInputStream() {
            @Override
            public boolean isFinished() {
                return false;
            }

            @Override
            public boolean isReady() {
                return false;
            }

            @Override
            public void setReadListener(ReadListener readListener) {

            }

            @Override
            public int read() throws IOException {
                return inputStream.read();
            }
        };
    }
}
