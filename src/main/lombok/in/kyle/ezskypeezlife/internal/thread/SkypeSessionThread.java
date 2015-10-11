package in.kyle.ezskypeezlife.internal.thread;

import in.kyle.ezskypeezlife.EzSkype;
import in.kyle.ezskypeezlife.internal.packet.session.SkypeActivePacket;
import in.kyle.ezskypeezlife.internal.packet.session.SkypeSessionPingPacket;

import java.util.concurrent.atomic.AtomicBoolean;

/**
 * Created by Kyle on 10/8/2015.
 */
public class SkypeSessionThread extends Thread {
    
    private EzSkype ezSkype;
    private AtomicBoolean active;
    
    public SkypeSessionThread(EzSkype ezSkype) {
        super("Skype-Session-Thread-" + ezSkype.getLocalUser().getUsername());
        this.ezSkype = ezSkype;
        this.active = ezSkype.getActive();
    }
    
    @Override
    public void run() {
        while (active.get()) {
            new SkypeActivePacket(ezSkype).executeAsync();
            new SkypeSessionPingPacket(ezSkype).executeAsync();
            try {
                Thread.sleep(10000);
            } catch (InterruptedException ignored) {
            }
        }
    }
}
