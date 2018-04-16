package com.ja.sur5ive;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Sur5ive {

    private static Sur5ive instance = new Sur5ive();

    public final String DOMAIN = "https://mysterious-sierra-76065.herokuapp.com";

    public final String REQUEST_CODE_PATH = "/validate/code/";
    public final String VALIDATE_CODE_PATH = "/validate/validatecode";
    public final String SEND_MESSAGE_PATH = "/sms/send";

    public final ExecutorService execPool = Executors.newFixedThreadPool(10);

    public static Sur5ive getInstance() {
        return instance;
    }

    private Sur5ive() {

    }

}
