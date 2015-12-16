package in.kyle.ezskypeezlife;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import in.kyle.ezskypeezlife.api.conversation.SkypeConversation;
import in.kyle.ezskypeezlife.api.errors.SkypeErrorHandler;
import in.kyle.ezskypeezlife.api.skype.SkypeCredentials;
import in.kyle.ezskypeezlife.api.skype.SkypeData;
import in.kyle.ezskypeezlife.api.skype.SkypeProperties;
import in.kyle.ezskypeezlife.api.user.SkypeLocalUser;
import in.kyle.ezskypeezlife.api.user.SkypeUser;
import in.kyle.ezskypeezlife.api.user.SkypeUserRole;
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
import in.kyle.ezskypeezlife.internal.obj.SkypeSession;
import in.kyle.ezskypeezlife.internal.packet.auth.SkypeAuthEndpointFinalPacket;
import in.kyle.ezskypeezlife.internal.packet.auth.SkypeAuthFinishPacket;
import in.kyle.ezskypeezlife.internal.packet.auth.SkypeJavascriptParams;
import in.kyle.ezskypeezlife.internal.packet.auth.SkypeLoginInfoPacket;
import in.kyle.ezskypeezlife.internal.packet.auth.SkypeLoginPacket;
import in.kyle.ezskypeezlife.internal.packet.conversation.SkypeConversationAddPacket;
import in.kyle.ezskypeezlife.internal.packet.conversation.SkypeConversationJoinPacket;
import in.kyle.ezskypeezlife.internal.packet.conversation.SkypeConversationJoinUrlIdPacket;
import in.kyle.ezskypeezlife.internal.packet.conversation.SkypeCreateConversationPacket;
import in.kyle.ezskypeezlife.internal.packet.conversation.SkypeGetConversationsPacket;
import in.kyle.ezskypeezlife.internal.packet.pull.SkypeRegisterEndpointsPacket;
import in.kyle.ezskypeezlife.internal.packet.ui.SkypeGetPagePackets;
import in.kyle.ezskypeezlife.internal.packet.user.SkypeGetContactsPacket;
import in.kyle.ezskypeezlife.internal.packet.user.SkypeGetSelfInfoPacket;
import in.kyle.ezskypeezlife.internal.packet.user.SkypeSignOutPacket;
import in.kyle.ezskypeezlife.internal.thread.SkypeContactsThread;
import in.kyle.ezskypeezlife.internal.thread.SkypePacketIOPool;
import in.kyle.ezskypeezlife.internal.thread.SkypePoller;
import in.kyle.ezskypeezlife.internal.thread.SkypeSessionKeepAlive;
import lombok.Getter;
import lombok.Setter;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.core.LoggerContext;
import org.apache.logging.log4j.core.config.Configuration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.List;
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
    private final AtomicLong messageId;
    @Getter
    private final SkypePacketIOPool packetIOPool;
    @Getter
    private final EventManager eventManager;
    @Getter
    private final AtomicBoolean active;
    @Getter
    private final SkypeCacheManager skypeCache;
    @Getter
    private SkypeSession skypeSession;
    @Getter
    private SkypeLocalUser localUser;
    @Getter
    @Setter
    private SkypeErrorHandler errorHandler;
    @Getter
    private SkypeProperties skypeProperties;
    @Getter
    private SkypeData skypeData;
    
    /**
     * Creates a new EzSkype instance with default properties
     *
     * @param skypeCredentials - The Skype login credentials
     */
    public EzSkype(SkypeCredentials skypeCredentials) {
        this(skypeCredentials, new SkypeProperties());
    }
    
    /**
     * Creates a new EzSkype instance
     *
     * @param skypeCredentials - Login credentials
     * @param skypeProperties  - Skype properties
     */
    public EzSkype(SkypeCredentials skypeCredentials, SkypeProperties skypeProperties) {
        this.skypeCredentials = skypeCredentials;
        this.active = new AtomicBoolean();
        this.skypeCache = new SkypeCacheManager(this);
        this.eventManager = new EventManager();
        this.packetIOPool = new SkypePacketIOPool(2);
        this.messageId = new AtomicLong(System.currentTimeMillis());
        this.skypeProperties = skypeProperties;
    }
    
    /**
     * Logs into Skype with a non guest account
     *
     * @return - The EzSkype instance
     * @throws IOException    - If a network error occurred
     * @throws SkypeException - If an unknown Skype error occurred
     */
    public EzSkype login() throws IOException, SkypeException {
        LOGGER.info("Logging into Skype, user: " + skypeCredentials.getUsername());
        // Get login params
    
        SkypeLoginInfoPacket loginInfoPacket = new SkypeLoginInfoPacket(this);
        SkypeJavascriptParams parameters = (SkypeJavascriptParams) loginInfoPacket.executeSync();
        
        // Get x-token
        SkypeLoginPacket skypeLoginPacket = new SkypeLoginPacket(this, skypeCredentials, parameters);
        String xToken = (String) skypeLoginPacket.executeSync();
        finishLogin(xToken);
        EzSkype.LOGGER.info("Loading conversations");
        loadConversations();
        return this;
    }
    
    private void finishLogin(String xToken) throws IOException, SkypeException {
        // Get reg token and location
        SkypeAuthFinishPacket skypeAuthFinishPacket = new SkypeAuthFinishPacket(this, xToken);
        skypeSession = (in.kyle.ezskypeezlife.internal.obj.SkypeSession) skypeAuthFinishPacket.executeSync();
        
        SkypeRegisterEndpointsPacket skypeRegisterEndpointsPacket = new SkypeRegisterEndpointsPacket(this, skypeProperties.getEndpoints());
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
        Map<String, SkypeConversationInternal> conversations = (Map) skypeGetConversationsPacket.executeSync();
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
            SkypeSessionKeepAlive sessionThread = new SkypeSessionKeepAlive(this, skypeProperties.getSessionPingInterval());
            new Thread(sessionThread).start();
            if (!skypeCredentials.isGuestAccount() && skypeProperties.isUpdateContacts()) {
                SkypeContactsThread contactsThread = new SkypeContactsThread(this, skypeProperties.getContactUpdateInterval());
                new Thread(contactsThread).start();
            }
        }
    }
    
    /**
     * Logs into a guest account
     *
     * @param url - The URL to join, eg: https://join.skype.com/xmky6Uk4TVfs
     */
    public EzSkype loginGuest(String url) throws IOException, SkypeException {
        String shortId = url.substring(url.lastIndexOf("/") + 1);
    
        SkypeWebClient webClient = new SkypeWebClient();
    
        SkypeGuestGetSessionIdPacket skypeGuestGetSessionIdPacket = new SkypeGuestGetSessionIdPacket(webClient, url);
        SkypeGuestTempSession tempSession = (SkypeGuestTempSession) skypeGuestGetSessionIdPacket.run();
    
        SkypeGuestGetSpaceIdPacket skypeGuestGetSpaceIdPacket = new SkypeGuestGetSpaceIdPacket(webClient, shortId);
        String spaceId = (String) skypeGuestGetSpaceIdPacket.run();
    
        SkypeGuestGetConversationIdPacket skypeGuestGetConversationId = new SkypeGuestGetConversationIdPacket(webClient, spaceId);
        String threadId = (String) skypeGuestGetConversationId.run();
    
        SkypeGuestGetTokenPacket skypeGuestGetTokenPacket = new SkypeGuestGetTokenPacket(webClient, tempSession, skypeCredentials
                .getUsername(), spaceId, threadId, shortId);
        String token = (String) skypeGuestGetTokenPacket.run();
        finishLogin(token);
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
     * Create a new Skype conversation
     *
     * @param members - All the members of the conversation, MAKE SURE TO INCLUDE YOURSELF!
     * @return - The conversation
     * @throws IOException    - If a network error occurred
     * @throws SkypeException - If an unknown error occurred
     */
    public SkypeConversation createSkypeConversation(Map<SkypeUser, SkypeUserRole> members) throws IOException, SkypeException {
        SkypeCreateConversationPacket skypeCreateConversationPacket = new SkypeCreateConversationPacket(this, members);
        return (SkypeConversation) skypeCreateConversationPacket.executeSync();
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
     * Gets Skype users from the provided usernames
     *
     * @param usernames - A list of usernames
     * @return - A list of loaded Skype users
     */
    public Map<String, SkypeUser> loadAll(List<String> usernames) throws IOException, SkypeException {
        return skypeCache.getUsersCache().getOrCreateUsersLoaded(usernames);
    }
    
    /**
     * Gets all the users Skype conversations
     *
     * @return - The users Skype conversations
     */
    public Map<String, SkypeConversation> getConversations() {
        return (Map) skypeCache.getConversationsCache().getSkypeConversations();
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
