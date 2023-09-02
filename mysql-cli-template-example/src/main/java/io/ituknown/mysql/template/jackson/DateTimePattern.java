package io.ituknown.mysql.template.jackson;

public final class DateTimePattern {
    /**
     * 20111203
     */
    public static final String BASIC_ISO_DATE = "yyyyMMdd";
    /**
     * 2011-12-03
     */
    public static final String ISO_LOCAL_DATE = "yyyy-MM-dd";
    /**
     * 10:15:30
     */
    public static final String ISO_LOCAL_TIME = "HH:mm:ss";
    /**
     * 2011-12-03T10:15:30
     */
    public static final String ISO_LOCAL_DATE_TIME = "yyyy-MM-dd'T'HH:mm:ss";
    /**
     * 2011-12-03 10:15:30
     */
    public static final String DEFAULT_LOCAL_DATE_TIME = "yyyy-MM-dd HH:mm:ss";
}