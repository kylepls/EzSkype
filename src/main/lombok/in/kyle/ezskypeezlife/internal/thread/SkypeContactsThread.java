package in.kyle.ezskypeezlife.internal.thread;

import in.kyle.ezskypeezlife.EzSkype;
import in.kyle.ezskypeezlife.api.obj.SkypeUser;
import in.kyle.ezskypeezlife.events.user.SkypeContactAddedEvent;
import in.kyle.ezskypeezlife.events.user.SkypeContactRemovedEvent;
import in.kyle.ezskypeezlife.internal.obj.SkypeLocalUserInternal;
import in.kyle.ezskypeezlife.internal.obj.SkypeUserInternal;
import in.kyle.ezskypeezlife.internal.packet.user.SkypeGetContactsPacket;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by Kyle on 10/9/2015.
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class SkypeContactsThread extends Thread {
    
    private final EzSkype ezSkype;
    private SkypeLocalUserInternal localUserInternal;
    
    public SkypeContactsThread(EzSkype ezSkype) {
        super("Skype-Contact-Poller-" + ezSkype.getLocalUser().getUsername());
        this.ezSkype = ezSkype;
        this.localUserInternal = ezSkype.getLocalUser();
    }
    
    @Override
    public void run() {
        while (ezSkype.getActive().get()) {
            try {
                SkypeGetContactsPacket contactsPacket = new SkypeGetContactsPacket(ezSkype);
                
                SkypeGetContactsPacket.UserContacts contacts = (SkypeGetContactsPacket.UserContacts) contactsPacket.executeSync();
                
                
                List<SkypeUserInternal> contactsNew = contacts.getContacts();
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
                
                // TODO this
                List<SkypeUser> pendingContacts = localUserInternal.getPendingContacts();
                pendingContacts.clear();
                pendingContacts.addAll(contacts.getPending().stream().map(skypeUserInternal -> ezSkype.getSkypeUser(skypeUserInternal
                        .getUsername())).collect(Collectors.toList()));
            } catch (Exception e) {
                EzSkype.LOGGER.error("Error while getting contacts", e);
            }
            try {
                Thread.sleep(5000);
            } catch (InterruptedException ignored) {
            }
        }
    }
}
