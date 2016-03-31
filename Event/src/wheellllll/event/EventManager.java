package wheellllll.event;

import java.util.HashMap;

/**
 * event manager manages events
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
        } else {
            args.put("event", "error");
            args.put("reason", "Event is illegal!");
            listeners.get("error").run(args);
        }
    }
}
