package in.kyle.ezskypeezlife;

import com.google.gson.Gson;
import in.kyle.ezskypeezlife.api.SkypeConversationType;
import in.kyle.ezskypeezlife.api.SkypeCredentials;
import in.kyle.ezskypeezlife.api.SkypeStatus;
import in.kyle.ezskypeezlife.api.obj.SkypeUser;
import in.kyle.ezskypeezlife.events.EventManager;
import in.kyle.ezskypeezlife.internal.caches.SkypeCacheManager;
import in.kyle.ezskypeezlife.internal.obj.*;
import in.kyle.ezskypeezlife.internal.packet.auth.SkypeAuthFinishPacket;
import in.kyle.ezskypeezlife.internal.packet.auth.SkypeLoginInfoPacket;
import in.kyle.ezskypeezlife.internal.packet.auth.SkypeLoginJavascriptParameters;
import in.kyle.ezskypeezlife.internal.packet.auth.SkypeLoginPacket;
import in.kyle.ezskypeezlife.internal.packet.conversation.SkypeGetConversationsPacket;
import in.kyle.ezskypeezlife.internal.packet.conversation.SkypeGetGroupConversationPacket;
import in.kyle.ezskypeezlife.internal.packet.pull.SkypeEndpoint;
import in.kyle.ezskypeezlife.internal.packet.pull.SkypeRegisterEndpointsPacket;
import in.kyle.ezskypeezlife.internal.packet.session.SkypeSetVisibilityPacket;
import in.kyle.ezskypeezlife.internal.packet.user.SkypeGetContactsPacket;
import in.kyle.ezskypeezlife.internal.packet.user.SkypeGetSelfInfoPacket;
import in.kyle.ezskypeezlife.internal.thread.SkypeContactsThread;
import in.kyle.ezskypeezlife.internal.thread.SkypePacketIOPool;
import in.kyle.ezskypeezlife.internal.thread.SkypePollerThread;
import in.kyle.ezskypeezlife.internal.thread.SkypeSessionThread;
import lombok.Getter;
import lombok.experimental.Accessors;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * Created by Kyle on 10/5/2015.
 */
public class EzSkype {
    
    public static final Gson GSON = new Gson();
    
    @Getter
    private SkypePacketIOPool packetIOPool;
    @Getter
    private SkypeSession skypeSession;
    @Getter
    private EventManager eventManager;
    @Getter
    private AtomicBoolean active;
    @Getter
    private SkypeCacheManager skypeCache;
    @Getter
    private SkypePollerThread skypePoller;
    @Getter
    private SkypeLocalUserInternal localUser;
    @Accessors
    private boolean lazyLoad = true;
    
    private SkypeCredentials skypeCredentials;
    
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
    }
    
    /**
     * Creates a new Skype instance (Noob constructor)
     *
     * @param user          - The Skype username
     * @param pass          - The Skype password
     */
    public EzSkype(String user, String pass) {
        this(new SkypeCredentials(user, pass.toCharArray()));
    }
    
    public void logout() {
        // TODO add logout packet
        active.set(false);
    }
    
    /**
     * Logs into Skype with specific endpoints
     *
     * @param endpoints - The endpoints Skype should listen for
     * @throws Exception
     */
    public EzSkype login(SkypeEndpoint[] endpoints) throws Exception {
        // Get login params
        SkypeLoginInfoPacket loginInfoPacket = new SkypeLoginInfoPacket(this);
        SkypeLoginJavascriptParameters parameters = (SkypeLoginJavascriptParameters) loginInfoPacket.executeSync();
        
        // Get x-token
        SkypeLoginPacket skypeLoginPacket = new SkypeLoginPacket(this, skypeCredentials, parameters);
        String xToken = (String) skypeLoginPacket.executeSync();
        
        // Get reg token and location
        SkypeAuthFinishPacket skypeAuthFinishPacket = new SkypeAuthFinishPacket(this, xToken);
        skypeSession = (SkypeSession) skypeAuthFinishPacket.executeSync();
        
        SkypeRegisterEndpointsPacket skypeRegisterEndpointsPacket = new SkypeRegisterEndpointsPacket(this, endpoints);
        skypeRegisterEndpointsPacket.executeSync();
        
        active.set(true);
        
        System.out.println("Loading profile");
        loadLocalProfile();
        System.out.println("Loading contacts");
        loadContacts();
        if (!lazyLoad) {
            loadConversations();
        }
        System.out.println("Starting poller");
        startThreads();
        return this;
    }
    
    /**
     * Logs into Skype with all endpoints
     *
     * @throws Exception
     */
    public EzSkype login() throws Exception {
        return login(SkypeEndpoint.values());
    }
    
    /**
     * // TODO this needs to be looked at
     *
     * @throws Exception
     */
    private void setOnline() throws Exception {
        System.out.println("Setting online");
        SkypeSetVisibilityPacket setVisibilityPacket = new SkypeSetVisibilityPacket(this, SkypeStatus.ONLINE);
        setVisibilityPacket.executeSync();
    }
    
    /**
     * Loads all contacts from the server
     *
     * @throws Exception
     */
    private void loadContacts() throws Exception {
        SkypeGetContactsPacket skypeGetContactsPacket = new SkypeGetContactsPacket(this);
        List<SkypeUserInternal> contacts = (List<SkypeUserInternal>) skypeGetContactsPacket.executeSync();
        getSkypeCache().getUsersCache().getSkypeUsers().addAll(contacts);
        localUser.getContacts().addAll(contacts);
    }
    
    /**
     * Loads all the conversations from the server
     *
     * @throws Exception
     */
    private void loadConversations() throws Exception {
        SkypeGetConversationsPacket skypeGetConversationsPacket = new SkypeGetConversationsPacket(this);
        List<String> conversationIds = (List<String>) skypeGetConversationsPacket.executeSync();
        
        List<SkypeConversationInternal> conversations = Collections.synchronizedList(new ArrayList<>());
        
        System.out.println("Loading " + conversationIds.size() + " conversations");
        
        ExecutorService executorService = Executors.newFixedThreadPool(10);// TODO configure login threads
        
        conversationIds.forEach((longId) -> {
            executorService.submit(() -> {
                SkypeConversationType conversationType = SkypeConversationType.fromLongId(longId);
                SkypeConversationInternal skypeConversationInternal;
                if (conversationType == SkypeConversationType.USER) {
                    skypeConversationInternal = new SkypeUserConversationInternal(this, longId, "Other " + "user", true, false, "");
                } else {
                    try {
                        SkypeGetGroupConversationPacket conversationPacket = new SkypeGetGroupConversationPacket(this, longId);
                        
                        skypeConversationInternal = (SkypeConversationInternal) conversationPacket.executeSync();
                    } catch (Exception e) {
                        System.err.println("Error getting conversation: " + conversationType + " - " + longId);
                        e.printStackTrace();
                        skypeConversationInternal = new SkypeConversationInternalEmpty(this, longId);
                    }
                }
                conversations.add(skypeConversationInternal);
            });
        });
        
        while (conversations.size() != conversationIds.size()) {
            System.out.println("Loading conversations: " + conversations.size() + "/" + conversationIds.size());
            try {
                Thread.sleep(5000);
            } catch (Exception ignored) {
            }
        }
        
        skypeCache.getConversationsCache().getSkypeConversations().addAll(conversations);
    }
    
    /**
     * Loads the users account from the server
     *
     * @throws Exception
     */
    private void loadLocalProfile() throws Exception {
        SkypeGetSelfInfoPacket getSelfInfoPacket = new SkypeGetSelfInfoPacket(this);
        localUser = (SkypeLocalUserInternal) getSelfInfoPacket.executeSync();
    }
    
    /**
     * Starts all the async threads
     */
    private void startThreads() {
        skypePoller = new SkypePollerThread(this);
        skypePoller.start();
        SkypeSessionThread sessionThread = new SkypeSessionThread(this);
        sessionThread.start();
        SkypeContactsThread contactsThread = new SkypeContactsThread(this);
        contactsThread.start();
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
     * Gets a Skype conversation from the LONG id
     * The long id should look like this
     * "19:3000ebdcfcca4b42b9f6964f4066e1ad@thread.skype"
     * "8:username"
     *
     * @param longId - The long id of the conversation
     * @return - A populated SkypeConversation class
     * - If the api did not return anything, a conversation with all empty fields except the username will be returned
     */
    public SkypeConversationInternal getSkypeConversation(String longId) {
        return skypeCache.getConversationsCache().getSkypeConversation(longId);
    }
}
