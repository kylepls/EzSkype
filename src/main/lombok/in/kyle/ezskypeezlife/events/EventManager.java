package in.kyle.ezskypeezlife.events;

import in.kyle.ezskypeezlife.EzSkype;
import lombok.Data;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class EventManager {
    
    private final List<HoldListener> listeners;
    
    public EventManager() {
        this.listeners = new ArrayList<>();
    }
    
    public void registerEvents(Object o) {
        for (Method m : o.getClass().getDeclaredMethods()) {
            if (m.getParameterCount() == 1) {
                Class type = m.getParameterTypes()[0];
                if (SkypeEvent.class.isAssignableFrom(type)) {
                    HoldListener holdListener = new HoldListener(o, m, type);
                    listeners.add(holdListener);
                }
            }
        }
    }
    
    public void unregisterEvents(Object o) {
        Iterator<HoldListener> listenerIterator = listeners.iterator();
        while (listenerIterator.hasNext()) {
            HoldListener listener = listenerIterator.next();
            if (o.equals(listener.getObject())) {
                listenerIterator.remove();
            }
        }
    }
    
    public void fire(SkypeEvent event) {
        //Will fire for abstracted events too if you choose to add them.
        listeners.stream().filter(listener -> listener.getEvent().isInstance(event)).forEach(listener -> {
            try {
                listener.getMethod().invoke(listener.getObject(), event);
            } catch (Exception e) {
                EzSkype.LOGGER.error("Error while firing event: " + listener, e);
            }
        });
    }
    
    @Data
    private static class HoldListener {
    
        private final Object object;
        private final Method method;
        private final Class event;
    }
}
