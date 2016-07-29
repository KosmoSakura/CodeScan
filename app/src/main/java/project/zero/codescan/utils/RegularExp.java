package project.zero.codescan.utils;

public class RegularExp {
    public static final String REGULAR_EXPRESSION_MOBILE = "^1(3[0-9]\\d|47\\d|5[0-9]\\d|7[67]\\d|8[0-9]\\d|70[059])\\d{7}$";

    public static final String REGULAR_EXPRESSION_PASSWORD = "^[a-z0-9A-Z~\\-_`!\\/@#$%\\^\\+\\*&\\\\?\\|:\\.<>\\[\\]{}()';=\",]*$";

    public static final String REGULAR_EXPRESSION_ID_CARD = "^[1-9]\\d{5}[1-9]\\d{3}((0\\d)|(1[0-2]))(([0|1|2]\\d)|3[0-1])\\d{3}([0-9]|X|x)$";

    public static final String REGULAR_EXPRESSION_REAL_NAME = "^[\\u4e00-\\u9fa5]+[·•●]{0,1}[\\u4e00-\\u9fa5]+$";

    public static final String REGULAR_EXPRESSION_CONTACT = "^[~\\-_`!\\/@#$%\\^\\+\\*&\\\\?\\|:\\.<>\\[\\]{}()';=\",]*$";

    public static final String REGUILAR_EXPERSSION_AMOUNT = "^(([1-9]\\d*)(\\.\\d{1,2})?)$|(0\\.0?([1-9]\\d?))$";

    public static final String REGULAR_EXPRESSION_TRADE_PASSWORD = "^(?![0-9]+$)(?![a-zA-Z]+$)[0-9A-Za-z]*$";

    public static final String REGULAR_EXPRESSION_ALL = ".+";
}
