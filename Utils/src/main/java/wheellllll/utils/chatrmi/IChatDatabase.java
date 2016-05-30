package wheellllll.utils.chatrmi;

import com.mongodb.BasicDBObject;
import org.bson.types.ObjectId;

/**
 * Created by sweet on 5/28/16.
 */
public interface IChatDatabase {
    public static int RMI_ID = Network.DATABASE;
    public BasicDBObject saveMessage(String message, String from);
    public boolean addUserToMessage(String username, ObjectId messageId);
    public boolean syncAccount(String username, ObjectId messageId);
}
