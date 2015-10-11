package in.kyle.ezskypeezlife;

import in.kyle.ezskypeezlife.api.SkypeCredentials;
import in.kyle.ezskypeezlife.internal.packet.pull.SkypeEndpoint;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Kyle on 10/10/2015.
 */
public class EzSkypeBuilder {
    
    
    private SkypeCredentials credentials;
    //private List<SkypeFeature> skypeFeatures;
    private List<SkypeEndpoint> skypeEndpoints;
    private int threads;
    private boolean saveSession;
    
    /**
     * Constructs a new EzSkype builder, this will build a Skype instance from scratch
     * If you don't enable a feature, events related to that feature will not fire
     * Use at your own risk
     *
     * @param credentials - The login credentials
     */
    public EzSkypeBuilder(SkypeCredentials credentials) {
        this.credentials = credentials;
        //this.skypeFeatures = new ArrayList<>();
        this.skypeEndpoints = new ArrayList<>();
        this.threads = 1;
    }
    
    /*
     * Adds a feature to Skype
     *
     * @param skypeFeature - The feature to be added
    public EzSkypeBuilder with(SkypeFeature skypeFeature) {
        skypeFeatures.add(skypeFeature);
        return this;
    }
     */
    
    /**
     * Adds a Skype endpoint to pull from
     *
     * @param skypeEndpoint - The endpoint to pull
     */
    public EzSkypeBuilder with(SkypeEndpoint skypeEndpoint) {
        skypeEndpoints.add(skypeEndpoint);
        return this;
    }
    
    /**
     * @param threads -How many threads should be used for packet io
     */
    public EzSkypeBuilder packetThreads(int threads) {
        this.threads = threads;
        return this;
    }
    
    /**
     * Creates the Skype instance and logs in
     *
     * @return - The EzSkype instance
     * @throws Exception
     */
    public EzSkype buildAndLogin() throws Exception {
        EzSkype ezSkype = new EzSkype(credentials, threads, saveSession);
        ezSkype.login(skypeEndpoints.toArray(new SkypeEndpoint[skypeEndpoints.size()]));
        
        return ezSkype;
    }
}
