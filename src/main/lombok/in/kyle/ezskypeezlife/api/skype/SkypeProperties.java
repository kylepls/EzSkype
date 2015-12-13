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
    
    private final long contactUpdateInterval;
    private final long sessionPingInterval;
    private final boolean updateContacts;
    private final SkypeEndpoint[] endpoints;
    private final Optional<Proxy> proxy;
    private final int packetIOThreads;
    
    public SkypeProperties() {
        this(20000L, 30000L, true, SkypeEndpoint.values(), Optional.empty(), 2);
    }
}
