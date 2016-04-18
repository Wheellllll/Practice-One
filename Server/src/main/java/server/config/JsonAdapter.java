package server.config;

import com.alibaba.fastjson.JSON;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * JSON 配置加载器
 */
public class JsonAdapter extends ConfigAdapter {

    /**
     * 从输入流读取所有内容
     *
     * @param is 输入流
     * @return 输入流中所有内容
     */
    private String convertStreamToString(InputStream is) {
        java.util.Scanner s = new java.util.Scanner(is).useDelimiter("\\A");
        return s.hasNext() ? s.next() : "";
    }

    /**
     * {@inheritDoc}
     */
    public <T> T loadToBean(InputStream streamIn, Class<T> clazz) {
        String content;
        content = convertStreamToString(streamIn);
        return JSON.parseObject(content, clazz);
    }

    /**
     * {@inheritDoc}
     */
    public void writeToStream(Object data, OutputStream streamOut) throws IOException {
        String content = JSON.toJSONString(data);
        streamOut.write(content.getBytes());
    }

}
