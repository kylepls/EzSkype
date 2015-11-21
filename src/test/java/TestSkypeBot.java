import in.kyle.ezskypeezlife.EzSkype;
import in.kyle.ezskypeezlife.api.SkypeCredentials;
import in.kyle.ezskypeezlife.api.SkypeUserRole;
import in.kyle.ezskypeezlife.api.captcha.SkypeCaptcha;
import in.kyle.ezskypeezlife.api.captcha.SkypeErrorHandler;
import in.kyle.ezskypeezlife.api.obj.SkypeConversation;
import in.kyle.ezskypeezlife.api.obj.SkypeMessage;
import in.kyle.ezskypeezlife.events.conversation.SkypeConversationAddedToEvent;
import in.kyle.ezskypeezlife.events.conversation.SkypeConversationCallEndedEvent;
import in.kyle.ezskypeezlife.events.conversation.SkypeConversationCallStartedEvent;
import in.kyle.ezskypeezlife.events.conversation.SkypeConversationKickedFromEvent;
import in.kyle.ezskypeezlife.events.conversation.SkypeConversationPictureChangeEvent;
import in.kyle.ezskypeezlife.events.conversation.SkypeConversationUpdateTopicEvent;
import in.kyle.ezskypeezlife.events.conversation.SkypeConversationUserJoinEvent;
import in.kyle.ezskypeezlife.events.conversation.SkypeConversationUserLeaveEvent;
import in.kyle.ezskypeezlife.events.conversation.SkypeConversationUserRoleUpdate;
import in.kyle.ezskypeezlife.events.conversation.SkypeMessageEditedEvent;
import in.kyle.ezskypeezlife.events.conversation.SkypeMessageReceivedEvent;
import in.kyle.ezskypeezlife.events.user.SkypeContactRequestEvent;
import org.apache.commons.lang.StringUtils;
import org.jsoup.Jsoup;

import java.io.File;
import java.io.FileReader;
import java.net.URL;
import java.util.Scanner;

/**
 * Created by Kyle on 10/8/2015.
 */
public class TestSkypeBot implements SkypeErrorHandler {
    
    private EzSkype ezSkype;
    
    public static void main(String[] args) throws Exception {
        new TestSkypeBot().startTest();
    }
    
    private void startTest() throws Exception {
        System.out.println("Logging in");
        
        // Load credentials from a file 'login.json'
        LoginCredentials loginCredentials = EzSkype.GSON.fromJson(new FileReader(new File("login.json")), LoginCredentials.class);
        
        // Enter the Skype login info here and login
        ezSkype = new EzSkype(new SkypeCredentials(loginCredentials.getUser(), loginCredentials.getPass()));
        ezSkype.setDebug(false);
        ezSkype.setErrorHandler(this);
        ezSkype.login();
        
        // Register all the events in this class
        // Events are denoted as methods that have 1 parameter that implements SkypeEvent
        ezSkype.getEventManager().registerEvents(this);
    
        System.out.println("Complete");
    }
    
    // Called when a new message is received from Skype
    public void onMessage(SkypeMessageReceivedEvent event) {
        String message = event.getMessage().getMessage(); // Get the message content
        System.out.println("Got message: " + event.getMessage().getSender().getUsername() + " - " + message);
        String[] args = message.split(" ");
    
        switch (args[0]) {
            case "+ping":  // Replies to a conversation with the message pong
                event.reply("Pong!");
                break;
            case "+me":  // Prints out user information
                event.reply("You: " + event.getMessage().getSender());
                break;
            case "+edit":  // Tests message editing
                SkypeMessage sendMessage = event.reply("Testing message " + "editing...");
                sendMessage.edit("Message editing works!");
                event.reply("Message edit test complete");
                break;
            case "+topic": // Sets the conversation topic
                if (args.length > 0) {
                    String topic = StringUtils.join(args, ' ', 1, args.length);
                    event.getMessage().getConversation().changeTopic(topic);
                    event.reply("Topic set to '" + topic + "'");
                } else {
                    event.reply("Usage: +topic topic");
                }
                break;
            case "+role": // Set your role
                if (args.length > 0) {
                    SkypeUserRole role = SkypeUserRole.valueOf(args[1].toUpperCase());
                    event.getMessage().getConversation().setUserRole(event.getMessage().getSender(), role);
                    event.reply("Set " + event.getMessage().getSender().getUsername() + " to " + role.name());
                } else {
                    event.reply("Usage: +role user|master");
                }
                break;
            case "+join": // Join a convo
                if (args.length > 0) {
                    String url = Jsoup.parse(StringUtils.join(args, ' ', 1, args.length)).text();
                    try {
                        SkypeConversation conversation = ezSkype.joinSkypeConversation(url);
                        event.reply("Joined: " + conversation.getTopic());
                    } catch (Exception e1) {
                        e1.printStackTrace();
                        event.reply("Error: " + event.getMessage());
                    }
                } else {
                    event.reply("Usage: +join url");
                }
                break;
            case "+contact": // Set user as a contact
                if (args.length > 0) {
                    boolean contact = Boolean.parseBoolean(args[1]);
                    event.getMessage().getSender().setContact(contact);
                    event.reply("Set contact: " + contact);
                } else {
                    event.reply("Usage: +contact boolean");
                }
                break;
            case "+info":
                event.reply(event.getMessage().getConversation().toString());
                // test convo loading
                event.getMessage().getConversation().fullyLoad();
                event.reply(event.getMessage().getConversation().toString());
                break;
            case "+convos":
                event.reply(ezSkype.getConversations().toString());
                break;
            case "+pic":
                if (args.length != 0) {
                    try {
                        String url = Jsoup.parse(StringUtils.join(args, ' ', 1, args.length)).text();
                        event.getMessage().getConversation().sendImage(new URL(url).openStream());
                    } catch (Exception e) {
                        e.printStackTrace();
                        event.reply("Could not send image, check console");
                    }
                    event.reply("Sent");
                } else {
                    event.reply("Usage: +pic url");
                }
                break;
        }
    }
    
    // Called when the topic of a conversation changes
    public void onTopic(SkypeConversationUpdateTopicEvent e) {
        e.getConversation().sendMessage("Topic changed\nFrom: " + e.getOldTopic() + "\nNew: " + e.getNewTopic());
    }
    
    // Called when a user joins a conversation
    public void onUserJoin(SkypeConversationUserJoinEvent e) {
        e.getConversation().sendMessage("User joined: " + e.getUser().getUsername());
    }
    
    // Called when a user leaves a conversation
    public void onUserLeave(SkypeConversationUserLeaveEvent e) {
        e.getConversation().sendMessage("User left: " + e.getUser().getUsername());
    }
    
    // Called when a user changes role in a conversation
    public void onRole(SkypeConversationUserRoleUpdate e) {
        e.getConversation().sendMessage("Role update: " + e.getUser().getUsername() + "\nFrom: " + e.getOldRole() +
                "\nTo: " + e.getNewRole() + "\nIs admin: " + e.getConversation().isAdmin(e.getUser()));
    
    }
    
    // Called when a call is initiated by a user
    public void onCallStart(SkypeConversationCallStartedEvent e) {
        e.getConversation().sendMessage("Call started by " + e.getUser().getUsername());
    }
    
    // Called when a call is ended
    public void onCallEnd(SkypeConversationCallEndedEvent e) {
        e.getConversation().sendMessage("Call ended by " + e.getUser().getUsername());
    }
    
    // Called when the bot is added to a conversation
    public void onAdd(SkypeConversationAddedToEvent e) {
        e.getConversation().sendMessage("Thanks for adding me to this conversation!");
    }
    
    // Called when the bot is removed from a conversation
    public void onRemove(SkypeConversationKickedFromEvent e) {
        System.out.println("I got removed from a conversation by " + e.getUser().getUsername());
    }
    
    // Called when a conversation picture is changed
    public void onPicture(SkypeConversationPictureChangeEvent e) {
        e.getConversation().sendMessage("Conversation picture changed\nFrom: " + e.getOldPicture() + "\nTo: " + e.getNewPicture());
    }
    
    // Called when a contact is added
    public void onContact(SkypeContactRequestEvent e) {
        System.out.println(e.getSkypeUser().getUsername() + " added me");
        e.getSkypeUser().setContact(true);
        e.getSkypeUser().sendMessage("Thanks for adding me as a contact!");
    }
    
    public void onEdit(SkypeMessageEditedEvent e) {
        e.getSkypeMessage().getConversation().sendMessage("Message edited\n From: " + e.getContentOld() + "\n To: " + e.getContentNew());
    }
    
    @Override
    public String solve(SkypeCaptcha skypeCaptcha) {
        System.out.println("Enter the solution to " + skypeCaptcha.getUrl() + " then click enter");
        Scanner scanner = new Scanner(System.in);
        return scanner.nextLine();
    }
    
    @Override
    public String setNewPassword() {
        System.out.println("Set new password!");
        return null;
    }
}
