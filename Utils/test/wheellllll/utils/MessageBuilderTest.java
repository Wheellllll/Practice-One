package wheellllll.utils;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import org.junit.Test;

import java.util.*;

import static org.junit.Assert.*;

/**
 * Created by Kris Chan on 10:42 PM 4/10/16 .
 * All right reserved.
 */
public class MessageBuilderTest {

    @Test
    public void testAdd() throws Exception {
        MessageBuilder msgBuilder = new MessageBuilder();
        msgBuilder.add("test","testbuild");
        String result = msgBuilder.buildString();
        HashMap<String,String> test = new HashMap<String,String>();
        test.put("test","testbuild");
        String expect = JSON.toJSONString(test);
        assertEquals(expect,result);

    }

    @Test
    public void testBuild() throws Exception {
        MessageBuilder msgBuilder = new MessageBuilder();
        String result = msgBuilder.buildString();
        assertEquals("{}",result);
    }

    @Test
    public void testStringToJSON() throws Exception {
        MessageBuilder msgBuilder = new MessageBuilder();
        String test = "test";
        JSONObject json1 = msgBuilder.StringToJSON(test);
        JSONObject json2 = new JSONObject();
        json2.get("test");
        assertEquals(json1,json2);
    }

    @Test
    public void testJsonToList() throws Exception {
        MessageBuilder msgBuilder = new MessageBuilder();
        JSONArray jsonArr = new JSONArray();
        Object test1 = new Object();
        jsonArr.add("test1");
        jsonArr.add("test2");
        test1 = msgBuilder.JsonToList(jsonArr);
        List test2 = new ArrayList<Object>();
        test2.add("test1");
        test2.add("test2");
        assertEquals(test1,test2);
    }

    @Test
    public void testJsonToMap() throws Exception {
        MessageBuilder msgBuilder = new MessageBuilder();
        JSONObject jsonObject = new JSONObject();
        Map<String, Object> test1 = new HashMap<String,Object>();
        jsonObject.get("test1");
        jsonObject.get("test2");
        test1 = msgBuilder.JsonToMap(jsonObject);
        Map<String, Object> test2 =  new HashMap<String,Object>();
        Set<String> jsonKeys = jsonObject.keySet();
        for (Object key : jsonKeys) {
            Object JsonValObj = jsonObject.get(key);
            if(JsonValObj instanceof JSONArray){
                test2.put((String)key,  msgBuilder.JsonToList((JSONArray) JsonValObj));
            }else if(key instanceof JSONObject){
                test2.put((String)key,  msgBuilder.JsonToMap((JSONObject) JsonValObj));
            }else{
                test2.put((String)key,JsonValObj);
            }
        }
        assertEquals(test1,test2);
    }

    @Test
    public void testMapKeys() throws Exception {
        MessageBuilder msgBuilder = new MessageBuilder();
        List<Object> test1 = new ArrayList<Object>();
        List<Object> test2 = new ArrayList<Object>();
        Object obj = new Object();
        obj = "test";
        test1 = msgBuilder.mapKeys(obj);
        Map<String,Object> columnValMap = new HashMap<String,Object>();
        String columnStr = "column";
        if(obj instanceof JSONArray){
            columnValMap =(Map<String, Object>)(((List<Object>)MessageBuilder.JsonToList((JSONArray) obj)).get(0));
        }
        else if(obj instanceof JSONObject){
            columnValMap =MessageBuilder.JsonToMap((JSONObject) obj);
        }
        else{
            test2.add(obj);
        }
        for(int i=0;i<columnValMap.keySet().size();i++){
            test2.add(columnStr+(i+1));
        }
        assertEquals(test1,test2);
    }

    public static void main(String args[]){
        
    }
}