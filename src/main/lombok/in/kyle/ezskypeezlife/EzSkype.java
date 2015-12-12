package in.kyle.ezskypeezlife;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import in.kyle.ezskypeezlife.api.SkypeCredentials;
import in.kyle.ezskypeezlife.api.captcha.SkypeErrorHandler;
import in.kyle.ezskypeezlife.api.obj.SkypeConversation;
import in.kyle.ezskypeezlife.api.obj.SkypeData;
import in.kyle.ezskypeezlife.api.obj.SkypeEndpoint;
import in.kyle.ezskypeezlife.api.obj.SkypeLocalUser;
import in.kyle.ezskypeezlife.api.obj.SkypeUser;
import in.kyle.ezskypeezlife.events.EventManager;
import in.kyle.ezskypeezlife.exception.SkypeException;
import in.kyle.ezskypeezlife.internal.caches.SkypeCacheManager;
import in.kyle.ezskypeezlife.internal.guest.SkypeGuestGetConversationIdPacket;
import in.kyle.ezskypeezlife.internal.guest.SkypeGuestGetSessionIdPacket;
import in.kyle.ezskypeezlife.internal.guest.SkypeGuestGetSpaceIdPacket;
import in.kyle.ezskypeezlife.internal.guest.SkypeGuestGetTokenPacket;
import in.kyle.ezskypeezlife.internal.guest.SkypeGuestTempSession;
import in.kyle.ezskypeezlife.internal.guest.SkypeWebClient;
import in.kyle.ezskypeezlife.internal.obj.SkypeConversationInternal;
import in.kyle.ezskypeezlife.internal.obj.SkypeLocalUserInternal;
import in.kyle.ezskypeezlife.internal.packet.auth.SkypeAuthEndpointFinalPacket;
import in.kyle.ezskypeezlife.internal.packet.auth.SkypeAuthFinishPacket;
import in.kyle.ezskypeezlife.internal.packet.auth.SkypeJavascriptParams;
import in.kyle.ezskypeezlife.internal.packet.auth.SkypeLoginInfoPacket;
import in.kyle.ezskypeezlife.internal.packet.auth.SkypeLoginPacket;
import in.kyle.ezskypeezlife.internal.packet.conversation.SkypeConversationAddPacket;
import in.kyle.ezskypeezlife.internal.packet.conversation.SkypeConversationJoinPacket;
import in.kyle.ezskypeezlife.internal.packet.conversation.SkypeConversationJoinUrlIdPacket;
import in.kyle.ezskypeezlife.internal.packet.conversation.SkypeGetConversationsPacket;
import in.kyle.ezskypeezlife.internal.packet.pull.SkypeRegisterEndpointsPacket;
import in.kyle.ezskypeezlife.internal.packet.ui.SkypeGetPagePackets;
import in.kyle.ezskypeezlife.internal.packet.user.SkypeGetContactsPacket;
import in.kyle.ezskypeezlife.internal.packet.user.SkypeGetSelfInfoPacket;
import in.kyle.ezskypeezlife.internal.packet.user.SkypeSignOutPacket;
import in.kyle.ezskypeezlife.internal.thread.SkypeContactsThread;
import in.kyle.ezskypeezlife.internal.thread.SkypePacketIOPool;
import in.kyle.ezskypeezlife.internal.thread.SkypePoller;
import in.kyle.ezskypeezlife.internal.thread.SkypeSession;
import lombok.Getter;
import lombok.Setter;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.core.LoggerContext;
import org.apache.logging.log4j.core.config.Configuration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.Proxy;
import java.util.Map;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicLong;

/**
 * Created by Kyle on 10/5/2015.
 */
public class EzSkype {
    
    public static final Gson GSON = new Gson();
    public static final Logger LOGGER = LoggerFactory.getLogger(EzSkype.class);
    
    @Getter
    private final SkypeCredentials skypeCredentials;
    @Getter
    private AtomicLong messageId;
    @Getter
    private SkypePacketIOPool packetIOPool;
    @Getter
    private in.kyle.ezskypeezlife.internal.obj.SkypeSession skypeSession;
    @Getter
    private EventManager eventManager;
    @Getter
    private AtomicBoolean active;
    @Getter
    private SkypeCacheManager skypeCache;
    @Getter
    private SkypeLocalUser localUser;
    @Getter
    @Setter
    private Proxy proxy;
    @Getter
    @Setter
    private SkypeErrorHandler errorHandler;
    
    @Getter
    private SkypeData skypeData;
    
    /**
     * Creates a new Skype instance (Noob constructor)
     *
     * @param user - The Skype username
     * @param pass - The Skype password
     */
    public EzSkype(String user, String pass) {
        this(new SkypeCredentials(user, pass.toCharArray()));
    }
    
    /**
     * Creates a new Skype instance (Pro constructor)
     *
     * @param skypeCredentials - The Skype login credentials
     */
    public EzSkype(SkypeCredentials skypeCredentials) {
        this.skypeCredentials = skypeCredentials;
        this.active = new AtomicBoolean();
        this.skypeCache = new SkypeCacheManager(this);
        this.eventManager = new EventManager();
        this.packetIOPool = new SkypePacketIOPool(2);
        this.messageId = new AtomicLong(System.currentTimeMillis());
    }
    
    /**
     * Logs into Skype with all endpoints and a non guest account
     */
    public EzSkype login() throws IOException, SkypeException {
        return login(SkypeEndpoint.values());
    }
    
    /**
     * Logs into Skype with specific endpoints
     *
     * @param endpoints - The endpoints Skype should listen for
     */
    public EzSkype login(SkypeEndpoint[] endpoints) throws IOException, SkypeException {
        LOGGER.info("Logging into Skype, user: " + skypeCredentials.getUsername());
        // Get login params
    
        SkypeLoginInfoPacket loginInfoPacket = new SkypeLoginInfoPacket(this);
        SkypeJavascriptParams parameters = (SkypeJavascriptParams) loginInfoPacket.executeSync();
        
        // Get x-token
        SkypeLoginPacket skypeLoginPacket = new SkypeLoginPacket(this, skypeCredentials, parameters);
        String xToken = (String) skypeLoginPacket.executeSync();
        finishLogin(endpoints, xToken);
        EzSkype.LOGGER.info("Loading conversations");
        loadConversations();
        return this;
    }
    
    private void finishLogin(SkypeEndpoint[] endpoints, String xToken) throws IOException, SkypeException {
        // Get reg token and location
        SkypeAuthFinishPacket skypeAuthFinishPacket = new SkypeAuthFinishPacket(this, xToken);
        skypeSession = (in.kyle.ezskypeezlife.internal.obj.SkypeSession) skypeAuthFinishPacket.executeSync();
        
        SkypeRegisterEndpointsPacket skypeRegisterEndpointsPacket = new SkypeRegisterEndpointsPacket(this, endpoints);
        skypeRegisterEndpointsPacket.executeSync();
        
        SkypeAuthEndpointFinalPacket finalPacket = new SkypeAuthEndpointFinalPacket(this);
        finalPacket.executeSync();
        
        active.set(true);
        
        EzSkype.LOGGER.info("Loading profile");
        loadLocalProfile();
        
        EzSkype.LOGGER.info("Loading Skype data");
        SkypeGetPagePackets skypeGetPagePacket = new SkypeGetPagePackets(null);
        skypeData = (SkypeData) skypeGetPagePacket.executeSync();
        
        if (!skypeCredentials.isGuestAccount()) {
            EzSkype.LOGGER.info("Loading contacts");
            loadContacts();
        }
        
        EzSkype.LOGGER.info("Starting pollers");
        startThreads();
    }
    
    private void loadConversations() throws IOException, SkypeException {
        SkypeGetConversationsPacket skypeGetConversationsPacket = new SkypeGetConversationsPacket(this);
        Map<String, SkypeConversationInternal> conversations = (Map<String, SkypeConversationInternal>) skypeGetConversationsPacket
                .executeSync();
        skypeCache.getConversationsCache().getSkypeConversations().putAll(conversations);
    }
    
    /**
     * Loads the users account from the server
     */
    private void loadLocalProfile() throws IOException, SkypeException {
        if (localUser == null) {
            SkypeGetSelfInfoPacket getSelfInfoPacket = new SkypeGetSelfInfoPacket(this);
            localUser = (SkypeLocalUserInternal) getSelfInfoPacket.executeSync();
        }
    }
    
    /**
     * Loads all contacts from the server
     */
    private void loadContacts() throws IOException, SkypeException {
        if (getSkypeCache().getUsersCache().getSkypeUsers().size() == 0) {
            SkypeGetContactsPacket skypeGetContactsPacket = new SkypeGetContactsPacket(this);
            SkypeGetContactsPacket.UserContacts contacts = (SkypeGetContactsPacket.UserContacts) skypeGetContactsPacket.executeSync();
            getSkypeCache().getUsersCache().getSkypeUsers().putAll(contacts.getContacts());
            localUser.getContacts().putAll(contacts.getContacts());
            localUser.getPendingContacts().putAll(contacts.getPending());
        }
    }
    
    /**
     * Starts all the async threads
     */
    private void startThreads() {
        if (active.get()) {
            SkypePoller skypePoller = new SkypePoller(this);
            new Thread(skypePoller).start();
            SkypeSession sessionThread = new SkypeSession(this);
            new Thread(sessionThread).start();
            if (!skypeCredentials.isGuestAccount()) {
                SkypeContactsThread contactsThread = new SkypeContactsThread(this);
                contactsThread.start();
            }
        }
    }
    
    /**
     * Logs into a guest account
     *
     * @param url - The URL to join, eg: https://join.skype.com/xmky6Uk4TVfs
     */
    public EzSkype loginGuest(SkypeEndpoint[] endpoints, String url) throws IOException, SkypeException {
        String shortId = url.substring(url.lastIndexOf("/") + 1);
    
        SkypeWebClient webClient = new SkypeWebClient();
    
        SkypeGuestGetSessionIdPacket skypeGuestGetSessionIdPacket = new SkypeGuestGetSessionIdPacket(webClient, url);
        SkypeGuestTempSession tempSession = (SkypeGuestTempSession) skypeGuestGetSessionIdPacket.run();
        //System.out.println("Session: " + tempSession);
    
        SkypeGuestGetSpaceIdPacket skypeGuestGetSpaceIdPacket = new SkypeGuestGetSpaceIdPacket(webClient, shortId);
        String spaceId = (String) skypeGuestGetSpaceIdPacket.run();
        //System.out.println("SpaceId: " + spaceId);
    
        SkypeGuestGetConversationIdPacket skypeGuestGetConversationId = new SkypeGuestGetConversationIdPacket(webClient, spaceId);
        String threadId = (String) skypeGuestGetConversationId.run();
        //System.out.println("ThreadId: " + threadId);
    
        SkypeGuestGetTokenPacket skypeGuestGetTokenPacket = new SkypeGuestGetTokenPacket(webClient, tempSession, skypeCredentials
                .getUsername(), spaceId, threadId, shortId);
        String token = (String) skypeGuestGetTokenPacket.run();
        //System.out.println("Token: " + token);
        finishLogin(endpoints, token);
        EzSkype.LOGGER.info("Loading conversations");
        loadConversations();
        return this;
    }
    
    public void logout() throws Exception {
        new SkypeSignOutPacket(this).executeSync();
        active.set(false);
    }
    
    /**
     * Joins a Skype conversation from the URL provided by Skype
     * This should look like https://join.skype.com/zzZzzZZzZZzZ
     *
     * @param skypeConversationUrl - The Skype conversation to join
     * @return - The Skype conversation joined
     */
    public SkypeConversation joinSkypeConversation(String skypeConversationUrl) throws IOException, SkypeException {
        String encodedId = (String) new SkypeConversationJoinUrlIdPacket(this, skypeConversationUrl).executeSync();
        JsonObject threadData = (JsonObject) new SkypeConversationJoinPacket(this, encodedId).executeSync();
        String longId = threadData.get("ThreadId").getAsString();
        SkypeConversationAddPacket addPacket = new SkypeConversationAddPacket(this, longId, localUser.getUsername());
        addPacket.executeSync();
        return getSkypeConversation(longId);
    }
    
    /**
     * Gets a Skype conversation from the LONG id
     * The long id should look like this
     * "19:3000ebdcfcca4b42b9f6964f4066e1ad@thread.skype"
     * "8:username"
     *
     * @param longId - The long id of the conversation
     * @return - A populated SkypeConversation class
     * - If the api did not return anything, a conversation with all empty fields except the username will be returned
     */
    public SkypeConversation getSkypeConversation(String longId) {
        return skypeCache.getConversationsCache().getSkypeConversation(longId);
    }
    
    /**
     * Gets a Skype user from their username
     * Will remove the 8: from the beginning if it is present
     *
     * @param username - The username of the user
     * @return - A populated SkypeUser class
     * - If the api did not return anything, a user with all empty fields except the username will be returned
     */
    public SkypeUser getSkypeUser(String username) {
        return skypeCache.getUsersCache().getOrCreateUserLoaded(username);
    }
    
    /**
     * Gets all the users Skype conversations
     *
     * @return - The users Skype conversations
     */
    public Map<String, SkypeConversation> getConversations() {
        return (Map<String, SkypeConversation>) (Object) skypeCache.getConversationsCache().getSkypeConversations();
    }
    
    public void setDebug(boolean debug) {
        Level level;
        if (debug) {
            level = Level.DEBUG;
        } else {
            level = Level.INFO;
        }
        LoggerContext ctx = (LoggerContext) LogManager.getContext(false);
        Configuration conf = ctx.getConfiguration();
        conf.getLoggerConfig(LogManager.ROOT_LOGGER_NAME).setLevel(level);
        ctx.updateLoggers(conf);
        LOGGER.info("Log level set to: {}", level);
    }
}
