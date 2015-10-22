package in.kyle.ezskypeezlife.internal.thread;

import in.kyle.ezskypeezlife.EzSkype;
import in.kyle.ezskypeezlife.internal.packet.SkypePacket;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

/**
 * Created by Kyle on 10/7/2015.
 */
public class SkypePacketIOPool {
    
    private final ExecutorService threadPool;
    
    public SkypePacketIOPool(int threads) {
        threadPool = Executors.newFixedThreadPool(threads);
    }
    
    public Future<Object> sendPacket(SkypePacket skypePacket) {
        return threadPool.submit(() -> {
            try {
                Thread.currentThread().setName("Skype-Packet-" + skypePacket.getClass().getName());
                return skypePacket.executeSync();
            } catch (Exception e) {
                EzSkype.LOGGER.error("Packet sending error, packet: " + skypePacket, e);
                return e;
            }
        });
    }
}
