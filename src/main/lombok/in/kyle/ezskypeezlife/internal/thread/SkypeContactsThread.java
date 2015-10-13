package in.kyle.ezskypeezlife.internal.thread;

import in.kyle.ezskypeezlife.EzSkype;
import in.kyle.ezskypeezlife.events.user.SkypeContactAddedEvent;
import in.kyle.ezskypeezlife.events.user.SkypeContactRemovedEvent;
import in.kyle.ezskypeezlife.internal.obj.SkypeLocalUserInternal;
import in.kyle.ezskypeezlife.internal.obj.SkypeUserInternal;
import in.kyle.ezskypeezlife.internal.packet.user.SkypeGetContactsPacket;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Kyle on 10/9/2015.
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class SkypeContactsThread extends Thread {
    
    private final EzSkype ezSkype;
    private SkypeLocalUserInternal localUserInternal;
    
    @Override
    public void run() {
        Thread.currentThread().setName("Skype-Contact-Poller-" + ezSkype.getLocalUser().getUsername());
        localUserInternal = ezSkype.getLocalUser();
        
        while (ezSkype.getActive().get()) {
            long start = System.currentTimeMillis();
            try {
                SkypeGetContactsPacket contactsPacket = new SkypeGetContactsPacket(ezSkype);
                
                List<SkypeUserInternal> contactsNew = (List<SkypeUserInternal>) contactsPacket.executeSync();
                
                List<SkypeUserInternal> contactsOld = new ArrayList<>(ezSkype.getLocalUser().getContacts());
                
                List<SkypeUserInternal> temp = new ArrayList<>(contactsNew);
                temp.removeAll(contactsOld);
                
                temp.forEach(contact -> {
                    EzSkype.LOGGER.debug("Got new contact: " + contact);
                    
                    SkypeUserInternal realUser = (SkypeUserInternal) ezSkype.getSkypeUser(contact.getUsername());
                    realUser.contact(true);
                    
                    localUserInternal.getContacts().add(realUser);
                    SkypeContactAddedEvent contactAddedEvent = new SkypeContactAddedEvent(realUser);
                    ezSkype.getEventManager().fire(contactAddedEvent);
                });
                
                temp = new ArrayList<>(contactsOld);
                temp.removeAll(contactsNew);
                
                temp.forEach(contact -> {
                    EzSkype.LOGGER.debug("Contact removed: " + contact);
    
                    SkypeUserInternal realUser = (SkypeUserInternal) ezSkype.getSkypeUser(contact.getUsername());
                    realUser.contact(true);
                    
                    localUserInternal.getContacts().remove(realUser);
                    SkypeContactRemovedEvent contactRemovedEvent = new SkypeContactRemovedEvent(realUser);
                    ezSkype.getEventManager().fire(contactRemovedEvent);
                });
                
                // check existing contacts for changes
                // TODO do I need this?
                contactsNew.retainAll(contactsOld);
                
                for (SkypeUserInternal contactNew : contactsNew) {
                    SkypeUserInternal contactOld = contactsOld.get(contactsOld.indexOf(contactNew));
        
                    if (contactOld.isContact() != contactNew.isContact()) {
                        EzSkype.LOGGER.debug("Contact status changed: " + contactOld.getUsername() + " " + contactOld.isContact() + " => " +
                                contactNew.getUsername() + " " + contactNew.isContact());
                    }
        
                    if (contactOld.isBlocked() != contactNew.isBlocked()) {
                        EzSkype.LOGGER.debug("Contact status changed: " + contactOld.getUsername() + " " + contactOld.isBlocked() + " => " +
                                contactNew.getUsername() + " " + contactNew.isBlocked());
                    }
                }
                
            } catch (Exception e) {
                EzSkype.LOGGER.error("Error while getting contacts", e);
            }
            long time = System.currentTimeMillis()-start; // TODO i might not need this
            try {
                long sleep = 5000-time;
                if (sleep > 0) { // Make sure to refresh contacts every 5 seconds or as close as we can get to that
                    Thread.sleep(sleep); 
                }
            } catch (InterruptedException ignored) {
            }
        }
    }
}
