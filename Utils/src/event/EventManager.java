package event;

import java.util.*;

/**
 * Created by sweet on 3/24/16.
 */
public class EventManager {
    private HashMap<String, EventListener> listeners;

    public EventManager() {
        listeners = new HashMap<>();
    }

    public EventManager addEventListener(String event, EventListener listener) {
        listeners.put(event, listener);
        return this;
    }

    public void triggerEvent(String event, HashMap<String, String> args) {
        EventListener listener = listeners.get(event);
        if (event != null) {
            listener.run(args);
        }
    }
}
