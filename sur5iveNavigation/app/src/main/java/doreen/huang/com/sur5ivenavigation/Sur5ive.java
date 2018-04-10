package doreen.huang.com.sur5ivenavigation;

public class Sur5ive {

    private static Sur5ive instance = new Sur5ive();

    public final String DOMAIN = "https://mysterious-sierra-76065.herokuapp.com";

    public final String REQUEST_CODE_PATH = "/validate/code/";
    public final String VALIDATE_CODE_PATH = "/validate/validatecode";
    public final String SEND_MESSAGE_PATH = "/sms/send";

    public static Sur5ive getInstance() {
        return instance;
    }

    private Sur5ive() {

    }

}
