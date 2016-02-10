package in.kyle.ezskypeezlife.api.skype;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.net.Proxy;
import java.util.Optional;

/**
 * Created by Kyle on 12/13/2015.
 */
@AllArgsConstructor
@Data
public class SkypeProperties {
    
    private long contactUpdateInterval;
    private long sessionPingInterval;
    private boolean updateContacts;
    private SkypeEndpoint[] endpoints;
    private Optional<Proxy> proxy;
    private int packetIOThreads;
    
    public SkypeProperties() {
        this(20000L, 30000L, true, SkypeEndpoint.values(), Optional.empty(), 2);
    }
}
