package wheellllll.utils;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import jdk.nashorn.internal.parser.JSONParser;

import java.util.*;

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
    public String buildString()
    {
        String jsonString = JSON.toJSONString(message);
        return jsonString;
    }

    public HashMap<String, String> buildMap()
    {
        return message;
    }

    /**
     *This method convert a <code>string</code> to a <code>JSON</code>,we need format the string first
     * @param jstring
     * @return
     */
    public JSONObject StringToJSON(String jstring){
        JSONObject json = new JSONObject();
        json.get(jstring);
        return json;
    }

    /**
     * This method convert a <code>JSONArray</code> to a <code>Map-List</code>
     * @param jsonArr
     * @return
     */
    public static Object JsonToList(JSONArray jsonArr){
        List<Object> jsonObjList = new ArrayList<Object> ();
        for(Object obj : jsonArr){
            if(obj instanceof JSONArray){
                jsonObjList.add(JsonToList((JSONArray) obj));
            } else if(obj instanceof JSONObject){
                jsonObjList.add(JsonToMap((JSONObject) obj));
            }else{
                jsonObjList.add(obj);
            }
        }
        return jsonObjList;
    }

    /**
     *  This method convert a <code>JSONObject</code> to a <code>Map-List</code>
     * @param json
     * @return
     */
    public static Map<String, Object> JsonToMap(JSONObject json){
        Map<String,Object> columnValMap = new HashMap<String,Object>();
        Set<String> jsonKeys = json.keySet();
        for (Object key : jsonKeys) {
            Object JsonValObj = json.get(key);
            if(JsonValObj instanceof JSONArray){
                columnValMap.put((String)key,  JsonToList((JSONArray) JsonValObj));
            }else if(key instanceof JSONObject){
                columnValMap.put((String)key,  JsonToMap((JSONObject) JsonValObj));
            }else{
                columnValMap.put((String)key,JsonValObj);
            }
        }
        return columnValMap;
    }


    /**
     * This method convert a <code>Objct</code> to a <code>Map-List</code>
     * @param obj
     * @return
     */
    public static List<Object> mapKeys(Object obj){
        List<Object> keysList = new ArrayList<Object>();
        Map<String,Object> columnValMap = new HashMap<String,Object>();
        String columnStr = "column";
        if(obj instanceof JSONArray){
            columnValMap =(Map<String, Object>)(((List<Object>)JsonToList((JSONArray) obj)).get(0));
        }
        else if(obj instanceof JSONObject){
            columnValMap =JsonToMap((JSONObject) obj);
        }
        else{
            keysList.add(obj);
        }
        for(int i=0;i<columnValMap.keySet().size();i++){
            keysList.add(columnStr+(i+1));
        }
        System.out.println(keysList.size());
        return keysList;
    }


}
