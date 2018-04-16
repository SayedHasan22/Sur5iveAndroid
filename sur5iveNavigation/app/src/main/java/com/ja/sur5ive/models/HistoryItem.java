package com.ja.sur5ive.models;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class HistoryItem {
    public static SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.CANADA);
    public Date timestamp;
    public String message;
}
