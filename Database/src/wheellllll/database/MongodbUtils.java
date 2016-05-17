package wheellllll.database;

import com.mongodb.*;
import com.mongodb.client.MongoDatabase;

import java.net.UnknownHostException;
import java.util.List;

import com.mongodb.BasicDBObject;
import com.sun.org.apache.xalan.internal.xsltc.runtime.BasisLibrary;

/**
 * Created by FanLiang on 5/17 0017.
 */
public class MongodbUtils {

    private static MongoClient mongoClient = null;
    private static DB db = null;
    private static DBCollection dbcollection = null;

    private static String dbName = "SoftwareReusePro";
    private static String collectionName = "Users";
    private static String hostName  = "localhost";
    private static int port = 27017;
    private static int poolSize = 10;

    public void setPoolSize( int _poolsize)  { this.poolSize = _poolsize;}
    public void setHostName(String _hostname){ this.hostName = _hostname;}
    public void setDbName(String _dbname)    { this.dbName   = _dbname;  }
    public void setCollectionName(String _collectionname){ this.collectionName = _collectionname;}

    private static void getConnection() {
        if( mongoClient==null ){
            MongoClientOptions.Builder build  = new MongoClientOptions.Builder();
            build.connectionsPerHost(poolSize);
            build.threadsAllowedToBlockForConnectionMultiplier(20);

            build.maxWaitTime(1000 * 60 * 2);
            build.connectTimeout(1000 * 60 * 1);

            MongoClientOptions mOptions = build.build();

            try{
                mongoClient = new MongoClient(hostName,mOptions);
            } catch(MongoException e1){
                e1.printStackTrace();
            } catch (Exception e2){
                e2.printStackTrace();
            }
        }

        db = mongoClient.getDB(dbName);
        dbcollection = db.getCollection(collectionName);
    }

    public static boolean isExisted(String username){
        BasicDBObject tempObj = new BasicDBObject();
        tempObj.put("username",username);
        try{
            getConnection();
            if(dbcollection.count(tempObj) > 0){
                return true;
            }else
                return false;
        }catch (Exception e){
            e.printStackTrace();
        }finally{
            dbcollection = null;
            db = null;
        }
        return false;
    }

    public static boolean isValid(String username,String password){
        if(!isExisted(username)){
            return false;
        }else {
            BasicDBObject tempObj = new BasicDBObject();
            tempObj.put("username", username);
            BasicDBObject passwordObj = new BasicDBObject();
            passwordObj.put("password",password);
            try {
                getConnection();
                List templist = dbcollection.find(tempObj).toArray();
                BasicDBObject realObj = (BasicDBObject) templist;
                if(realObj.get("password").equals(passwordObj.get("password"))) {
                    return true;
                } else {
                    return false;
                }
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                dbcollection = null;
                db = null;
            }
            return false;
        }
    }

    public static boolean createAccount(String username, String password, int groupid) {
        BasicDBObject obj = new BasicDBObject();
        obj.put("username",username);
        obj.put("password",password);
        obj.put("groupid",groupid);
        try {
            getConnection();
            dbcollection.insert(obj);
        }catch(Exception e){
            e.printStackTrace();
        }finally {
            dbcollection = null;
            db = null;
        }
        return true;
    }

    public static boolean changeGroupId(String username, String password, int newGroupId) {
        BasicDBObject tempObj = new BasicDBObject();
        tempObj.put("username",username);
        tempObj.put("password", password);

        try {
            getConnection();
            List templist = dbcollection.find(tempObj).toArray();

            BasicDBObject oldObj = (BasicDBObject) templist;
            BasicDBObject newObj = (BasicDBObject) oldObj.clone();
            newObj.put("groupid", newGroupId);

            dbcollection.update(oldObj, newObj);
        }catch(Exception e){
            e.printStackTrace();
        }finally {
            dbcollection = null;
            db = null;
        }
        return true;
    }






}

