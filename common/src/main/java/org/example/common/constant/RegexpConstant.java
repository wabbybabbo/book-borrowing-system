package org.example.common.constant;

import static org.example.common.constant.GenderConstant.FEMALE;
import static org.example.common.constant.GenderConstant.MALE;

/**
 * 正则表达式常量
 */
public class RegexpConstant {

    public static final String ADMIN_NAME = "^\\S{2,16}$";
    public static final String USER_NAME = "^\\S{2,16}$";
    public static final String ACCOUNT = "^\\w{4,16}$";
    public static final String PASSWORD = "^\\w{4,16}$";
    public static final String GENDER = "^[" + MALE + FEMALE + "]$";
    public static final String PHONE = "^[1-9][0-9]{10}$";
    public static final String EMAIL = "^[a-zA-Z0-9_-]+@[a-zA-Z0-9_-]+(\\.[a-zA-Z0-9_-]+)+$";
    /**
     * 匹配规则：
     * 字符串中不可出现除了空格以外的空白字符；字符串中不可出现连续的空格；字符串两端不可出现空白字符
     */
    public static final String BOOK_NAME = "^(\\S+[ ]?)+\\S$";
    public static final String ISBN = "^(978|979)\\d{10}$";
    public static final String AUTHOR = "^(\\S+[ ]?)+\\S$";
    public static final String PUBLISHER = "^(\\S+[ ]?)+\\S$";
    public static final String CATEGORY_NAME = "^\\S{2,16}$";
}
