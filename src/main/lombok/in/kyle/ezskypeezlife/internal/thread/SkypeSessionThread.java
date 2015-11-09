package in.kyle.ezskypeezlife.internal.thread;

import in.kyle.ezskypeezlife.EzSkype;
import in.kyle.ezskypeezlife.internal.packet.session.SkypeActivePacket;
import in.kyle.ezskypeezlife.internal.packet.session.SkypeSessionExpiredException;
import in.kyle.ezskypeezlife.internal.packet.session.SkypeSessionPingPacket;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * Created by Kyle on 10/8/2015.
 */
public class SkypeSessionThread extends Thread {
    
    private final EzSkype ezSkype;
    private final AtomicBoolean active;
    
    public SkypeSessionThread(EzSkype ezSkype) {
        super("Skype-Session-Thread-" + ezSkype.getLocalUser().getUsername());
        this.ezSkype = ezSkype;
        this.active = ezSkype.getActive();
    }
    
    @Override
    public void run() {
        while (active.get()) {
            try {
                new SkypeSessionPingPacket(ezSkype).executeSync();
                new SkypeActivePacket(ezSkype).executeSync();
            } catch (SkypeSessionExpiredException exception) {
    
                try {
                    EzSkype.LOGGER.info("Skype session expired, logging in again...");
                    ezSkype.login();
                } catch (Exception e) {
                    EzSkype.LOGGER.error("Could not login to Skype again, shutting down", e);
                    active.set(false);
                    return;
                }
            } catch (Exception e) {
                EzSkype.LOGGER.error("Error sending session ping packet", e);
            }
    
            try {
                Thread.sleep(TimeUnit.SECONDS.toMillis(30));
            } catch (InterruptedException ignored) {
            }
        }
    }
}
