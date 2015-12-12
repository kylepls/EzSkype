import com.google.gson.JsonObject;
import in.kyle.ezskypeezlife.EzSkype;
import in.kyle.ezskypeezlife.api.SkypeCredentials;
import in.kyle.ezskypeezlife.api.SkypeStatus;
import in.kyle.ezskypeezlife.api.SkypeUserRole;
import in.kyle.ezskypeezlife.api.captcha.SkypeCaptcha;
import in.kyle.ezskypeezlife.api.captcha.SkypeErrorHandler;
import in.kyle.ezskypeezlife.api.obj.SkypeConversation;
import in.kyle.ezskypeezlife.api.obj.SkypeMessage;
import in.kyle.ezskypeezlife.api.obj.SkypeUser;
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
import in.kyle.ezskypeezlife.exception.SkypeException;
import org.apache.commons.lang.StringUtils;
import org.jsoup.Jsoup;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.net.URL;
import java.util.Scanner;

/**
 * Created by Kyle on 10/8/2015.
 */
@SuppressWarnings("unused")
public class TestSkypeBot implements SkypeErrorHandler {
    
    private EzSkype ezSkype;
    
    public static void main(String[] args) throws IOException, SkypeException {
        new TestSkypeBot().startTest();
    }
    
    private void startTest() throws IOException, SkypeException {
        System.out.println("Logging in");
        
        // Load credentials from a file 'login.json'
        JsonObject login = EzSkype.GSON.fromJson(new FileReader(new File("login.json")), JsonObject.class);
        
        // Enter the Skype login info here
        ezSkype = new EzSkype(new SkypeCredentials(login.get("user").getAsString(), login.get("pass").getAsString()));
        ezSkype.setDebug(true);
        
        // A error handler is a class that will be called to solve issues with the bot
        ezSkype.setErrorHandler(this);
        ezSkype.login();
        ezSkype.getLocalUser().setStatus(SkypeStatus.ONLINE);
        
        // Register all the events in this class
        // Events are denoted as methods that have 1 parameter that implements SkypeEvent
        ezSkype.getEventManager().registerEvents(this);
        
        System.out.println("Logged in");
    }
    
    // Called when a new message is received from Skype
    public void onMessage(SkypeMessageReceivedEvent event) {
        String message = event.getMessage().getMessage(); // Get the message content
        System.out.println("Got message: " + event.getMessage().getSender().getUsername() + " - " + message);
        String[] args = message.split(" ");
    
        // Some basic command implementations
        switch (args[0].toLowerCase()) {
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
            case "+join": // Join a conversation by the Skype join URL, eg: https://join.skype.com/rbjoWQCc9b8l
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
            case "+info": // Get current conversation information
                event.reply(event.getMessage().getConversation().toString());
                // Specific details about a conversation (The topic, who made it, all the users in it) may not have already been loaded
                // Calling fullyLoad will load in the important data we need if it is not already loaded
                event.getMessage().getConversation().fullyLoad();
                event.reply(event.getMessage().getConversation().toString());
                break;
            case "+convos": // List all the conversations the bot is in
                event.reply(ezSkype.getConversations().toString());
                break;
            case "+pic": // Send a ping of an image from a URL
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
    public void onTopic(SkypeConversationUpdateTopicEvent event) {
        event.getConversation().sendMessage("Topic changed\nFrom: " + event.getOldTopic() + "\nNew: " + event.getNewTopic());
    }
    
    // Called when a user joins a conversation
    public void onUserJoin(SkypeConversationUserJoinEvent event) {
        event.getConversation().sendMessage("User joined: " + event.getUser().getUsername());
    }
    
    // Called when a user leaves a conversation
    public void onUserLeave(SkypeConversationUserLeaveEvent event) {
        event.getConversation().sendMessage("User left: " + event.getUser().getUsername());
    }
    
    // Called when a user changes role in a conversation
    public void onRole(SkypeConversationUserRoleUpdate event) {
        event.getConversation().sendMessage("Role update: " + event.getUser().getUsername() + "\nFrom: " + event.getOldRole() +
                "\nTo: " + event.getNewRole() + "\nIs admin: " + event.getConversation().isAdmin(event.getUser()));
    }
    
    // Called when a call is initiated by a user
    public void onCallStart(SkypeConversationCallStartedEvent event) {
        event.getConversation().sendMessage("Call started by " + event.getUser().getUsername());
    }
    
    // Called when a call is ended
    public void onCallEnd(SkypeConversationCallEndedEvent event) {
        event.getConversation().sendMessage("Call ended by " + event.getUser().getUsername());
    }
    
    // Called when the bot is added to a conversation
    public void onAdd(SkypeConversationAddedToEvent event) {
        event.getConversation().sendMessage("Thanks for adding me to this conversation!");
    }
    
    // Called when the bot is removed from a conversation
    public void onRemove(SkypeConversationKickedFromEvent event) {
        System.out.println("I got removed from a conversation by " + event.getUser().getUsername());
    }
    
    // Called when a conversation picture is changed
    public void onPicture(SkypeConversationPictureChangeEvent event) {
        event.getConversation().sendMessage("Conversation picture changed\nFrom: " + event.getOldPicture() + "\nTo: " + event
                .getNewPicture());
    }
    
    // Called when someone sends a contact request
    public void onContact(SkypeContactRequestEvent event) {
        SkypeUser user = event.getUser();
        System.out.println(user.getUsername() + " added me");
        user.setContact(true);
        user.sendMessage("Thanks for adding me as a contact!");
    }
    
    // Called when a user edits a message
    public void onEdit(SkypeMessageEditedEvent event) {
        event.getConversation().sendMessage("Message edited\n From: " + event.getContentOld() + "\n To: " + event.getContentNew());
    }
    
    // Called when a captcha needs to be solved
    // Returns the solution to the captcha
    @Override
    public String solve(SkypeCaptcha skypeCaptcha) {
        System.out.println("Enter the solution to " + skypeCaptcha.getUrl() + " then click enter");
        Scanner scanner = new Scanner(System.in);
        return scanner.nextLine();
    }
    
    // Called when the Skype password needs to be changed (Skype forces you)
    // Returns the new password and null if no new password will be set
    @Override
    public String setNewPassword() {
        System.out.println("Set new password!");
        return null;
    }
}
