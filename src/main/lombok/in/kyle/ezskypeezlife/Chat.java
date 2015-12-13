package in.kyle.ezskypeezlife;

import in.kyle.ezskypeezlife.api.conversation.message.SkypeEmoji;
import lombok.experimental.UtilityClass;
import org.apache.commons.lang3.StringEscapeUtils;

import java.awt.*;

/**
 * Created by Kyle on 10/11/2015.
 * <p>
 * Used for formatting text
 */
@UtilityClass
public class Chat {
    
    /**
     * Makes the text blink for the client
     *
     * @param text - The text to format
     * @return - The formatted string
     */
    public static String blink(final String text) {
        return "<blink>" + text + "</blink>";
    }
    
    /**
     * Bolds a string of text
     *
     * @param text - The text to bold
     * @return - The formatted text
     */
    public static String bold(final String text) {
        return "<b>" + text + "</b>";
    }
    
    /**
     * Makes the text monospace meaning that each character will occupy the same amount of space
     *
     * @param text - The text to apply the effect to
     * @return - The formatted text
     */
    public static String code(final String text) {
        return "<pre>" + text + "</pre>";
    }
    
    /**
     * Applies a color to a string of text
     *
     * @param text  - The text to format
     * @param color - The color of the text
     * @return - The formatted string
     */
    public static String color(final String text, final Color color) {
        String hex = String.format("#%02x%02x%02x", color.getRed(), color.getGreen(), color.getBlue());
        return "<font color=\"" + hex + "\">" + text + "</font>";
    }
    
    /**
     * Adds an emoji
     *
     * @param skypeEmoji - The emjoi to be converted to text
     * @return - The formatted string
     */
    public static String emoji(final SkypeEmoji skypeEmoji) {
        return emoji(skypeEmoji.getShortcuts().get(0));
    }
    
    /**
     * Adds an emoji
     *
     * @param emoji - The emjoi id
     * @return - The formatted string
     */
    public static String emoji(final String emoji) {
        return "<ss type=\"" + emoji + "\">" + emoji + "</ss>";
    }
    
    /**
     * Encode html tags and whatnot
     *
     * @param text - The text to format
     * @return - The formatted string
     */
    public static String encodeRawText(String text) {
        return StringEscapeUtils.escapeXml11((StringEscapeUtils.escapeJson(text)));
    }
    
    /**
     * Italicises a string of text
     *
     * @param text - The text to format
     * @return - The formatted string
     */
    public static String italic(final String text) {
        return "<i>" + text + "</i>";
    }
    
    /**
     * Makes the string into a link
     *
     * @param text - The String to link
     * @param url  - The destination URL
     * @return - The formatted string
     */
    public static String link(final String text, final String url) {
        return "<a href=\"" + url + "\">" + text + "</a>";
    }
    
    /**
     * Sets the size of a string of text
     *
     * @param text - The text to size
     * @param size - The font size of the text
     * @return - The formatted text
     */
    public static String size(final String text, final int size) {
        return "<font size=\"" + size + "\">" + text + "</font>";
    }
    
    public static String encodeTags(String text) {
        return text.replace("<", StringEscapeUtils.escapeXml11((StringEscapeUtils.escapeJson("<"))))
                .replace(">", StringEscapeUtils.escapeXml11((StringEscapeUtils.escapeJson(">"))));
    }
    
    /**
     * Draws a line thorough text
     *
     * @param text - The text to format
     * @return - The formatted string
     */
    public static String strikeThrough(final String text) {
        return "<s>" + text + "</s>";
    }
    
    /**
     * Underline text
     *
     * @param text - The text to format
     * @return - The formatted string
     */
    public static String underline(final String text) {
        return "<u>" + text + "</u>";
    }
}
