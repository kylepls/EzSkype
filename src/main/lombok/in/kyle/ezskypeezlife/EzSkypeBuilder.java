package in.kyle.ezskypeezlife;

import in.kyle.ezskypeezlife.api.SkypeCredentials;
import in.kyle.ezskypeezlife.api.captcha.SkypeErrorHandler;
import in.kyle.ezskypeezlife.api.obj.SkypeEndpoint;

import java.net.Proxy;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by Kyle on 10/10/2015.
 */
public class EzSkypeBuilder {
    
    private final SkypeCredentials credentials;
    private final List<SkypeEndpoint> skypeEndpoints;
    private String url;
    private SkypeErrorHandler skypeErrorHandler;
    private boolean debug;
    private Proxy proxy;
    
    /**
     * This constructor is for guest accounts only, the URL is the url to join the Skype chat
     * Constructs a new EzSkype builder, this will build a Skype instance from scratch
     * If you don't enable a feature, events related to that feature will not fire
     *
     * @param username - The desired username
     * @param url      - The url to join the Skype chat
     */
    public EzSkypeBuilder(String username, String url) {
        this(new SkypeCredentials(username));
        this.url = url;
    }
    
    /**
     * Constructs a new EzSkype builder, this will build a Skype instance from scratch
     * If you don't enable a feature, events related to that feature will not fire
     *
     * @param credentials - The login credentials
     */
    public EzSkypeBuilder(SkypeCredentials credentials) {
        this.credentials = credentials;
        this.skypeEndpoints = new ArrayList<>();
        this.url = null;
    }
    
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
     * Adds all Skype endpoints to the builder
     */
    public EzSkypeBuilder withAll() {
        skypeEndpoints.addAll(Arrays.asList(SkypeEndpoint.values()));
        return this;
    }
    
    public EzSkypeBuilder debug(boolean debugMode) {
        debug = debugMode;
        return this;
    }
    
    /**
     * Adds a handler that is called when EzSkype encounters a captcha on login
     *
     * @param skypeErrorHandler - The captcha solver
     */
    public EzSkypeBuilder setCaptchaHandler(SkypeErrorHandler skypeErrorHandler) {
        this.skypeErrorHandler = skypeErrorHandler;
        return this;
    }
    
    /**
     * Sets a proxy to be used with all outgoing connections
     *
     * @param proxy - The proxy to be used
     */
    public EzSkypeBuilder setProxy(Proxy proxy) {
        this.proxy = proxy;
        return this;
    }
    
    /**
     * Creates the Skype instance and logs in
     *
     * @return - The EzSkype instance
     * @throws Exception
     */
    public EzSkype buildAndLogin() throws Exception {
        if (skypeEndpoints.size() == 0) {
            throw new RuntimeException("Cannot build Skype instance with 0 endpoints, use .with to add an endpoint");
        }
        
        EzSkype ezSkype = new EzSkype(credentials);
        ezSkype.setDebug(debug);
        if (proxy != null) {
            ezSkype.setProxy(proxy);
        }
        ezSkype.setErrorHandler(skypeErrorHandler);
        SkypeEndpoint[] endpoints = skypeEndpoints.toArray(new SkypeEndpoint[skypeEndpoints.size()]);
        if (credentials.isGuestAccount()) {
            ezSkype.loginGuest(endpoints, url);
        } else {
            ezSkype.login(endpoints);
        }
        
        return ezSkype;
    }
}
