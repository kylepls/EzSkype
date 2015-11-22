package in.kyle.ezskypeezlife.events;

import in.kyle.ezskypeezlife.api.obj.SkypeUser;

/**
 * Created by Kyle on 11/21/2015.
 * <p>
 * An event that is called when a user invokes an action
 */
public interface SkypeUserEvent {
    
    /**
     * @return - The user that invoked the event
     */
    SkypeUser getUser();
    
}
