package in.kyle.ezskypeezlife.api.captcha;

/**
 * Created by Kyle on 11/5/2015.
 */
public interface SkypeCaptchaHandler {
    
    /**
     * Returns a result to the given captcha
     *
     * @param skypeCaptcha - The captcha to sole
     * @return - The solution to the captcha
     */
    String solve(SkypeCaptcha skypeCaptcha);
    
}
