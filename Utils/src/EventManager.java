import java.lang.reflect.Method;
import java.util.HashMap;

/**
 * Created by sweet on 3/21/16.
 */
public class EventManager {

    static volatile EventManager defaultInstance;

    private HashMap<String, Method> mSubscriptionByEventName;

    public EventManager() {
        mSubscriptionByEventName = new HashMap<>();
    }

    public static EventManager getDefault() {
        if (defaultInstance == null) {
            synchronized (EventManager.class) {
                if (defaultInstance == null) {
                    defaultInstance = new EventManager();
                }
            }
        }
        return defaultInstance;
    }

    public void init() {

    }

    public void subscribe(String key, Method method) {
        mSubscriptionByEventName.put(key, method);
    }

    public void trigger(String event) {
        for (String key : mSubscriptionByEventName.keySet()) {
            if (key.equals(event)) {

            }
        }
    }
}
