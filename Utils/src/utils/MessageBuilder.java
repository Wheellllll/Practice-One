package utils;

import com.alibaba.fastjson.JSON;

import java.util.HashMap;

/**
 * Created by summer on 3/23/16.
 */
public class MessageBuilder {
    private HashMap<String,String> message = new HashMap<>();
    public MessageBuilder add(String key, String value)
    {
        message.put(key, value);
        return this;
    }

    
    public String build()
    {
        String jsonString = JSON.toJSONString(message);
        return jsonString;
    }

}
