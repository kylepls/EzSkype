package in.kyle.ezskypeezlife.events;

import in.kyle.ezskypeezlife.EzSkype;
import lombok.Data;
import lombok.Getter;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Created by Kyle on 9/5/2015.
 */
public class EventManager {
    
    @Getter
    private final List<HoldListener> listeners;
    
    public EventManager() {
        this.listeners = new ArrayList();
    }
    
    public void registerEvents(Object o) {
        for (Method m : o.getClass().getDeclaredMethods()) {
            if (m.getParameterCount() == 1) {
                Class type = m.getParameterTypes()[0];
                if (SkypeEvent.class.isAssignableFrom(type)) {
                    m.setAccessible(true);
                    HoldListener holdListener = new HoldListener(o, m, type);
                    listeners.add(holdListener);
                }
            }
        }
    }
    
    public void unregisterEvents(Object o) {
        synchronized (listeners) {
            Iterator<HoldListener> listenerIterator = listeners.iterator();
            while (listenerIterator.hasNext()) {
                HoldListener listener = listenerIterator.next();
                if (o.equals(listener.getObject())) {
                    listenerIterator.remove();
                }
            }
        }
    }
    
    @SuppressWarnings("ForLoopReplaceableByForEach")
    public void fire(SkypeEvent event) {
        HoldListener holdListener;
        for (int i = 0; i < listeners.size(); i++) {
            holdListener = listeners.get(i);
            synchronized (holdListener.getObject()) {
                if (holdListener.getEvent().equals(event.getClass())) {
                    try {
                        holdListener.getMethod().invoke(holdListener.getObject(), event);
                    } catch (Exception e) {
                        EzSkype.LOGGER.error("Error while firing event: " + holdListener, e);
                    }
                }
            }
        }
    }
    
    @Data
    public static class HoldListener {
        
        private final Object object;
        private final Method method;
        private final Class event;
    }
}
