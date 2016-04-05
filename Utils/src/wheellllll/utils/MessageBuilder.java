package wheellllll.utils;

import com.alibaba.fastjson.JSON;

import java.util.HashMap;

/**
 * This class provide methods for building message
 */
public class MessageBuilder {
    private HashMap<String,String> message = new HashMap<>();

    /**
     *
     * @param key Json key
     * @param value Json value
     * @return MessageBuilder
     */
    public MessageBuilder add(String key, String value)
    {
        message.put(key, value);
        return this;
    }

    /**
     *
     * @return Json string
     */
    public String build()
    {
        String jsonString = JSON.toJSONString(message);
        return jsonString;
    }

}
