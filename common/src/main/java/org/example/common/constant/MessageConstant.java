package org.example.common.constant;

/**
 * 信息提示常量类
 */
public class MessageConstant {

    public static final String LOGIN_SUCCESS = "登录成功";
    public static final String REGISTER_SUCCESS = "注册成功";
    public static final String UPDATE_SUCCESS = "更改成功";
    public static final String BORROW_SUCCESS = "借阅成功";
    public static final String CANCEL_SUCCESS = "取消成功";
    public static final String DELETE_SUCCESS = "删除成功";
    public static final String CREATE_SUCCESS = "新建成功";
    public static final String DISABLE_SUCCESS = "禁用成功";
    public static final String ENABLE_SUCCESS = "启用成功";

    public static final String FIELD_NOT_NULL = "不能为空";
    public static final String FIELD_NOT_EMPTY = "不能为空";
    public static final String FIELD_NOT_BLANK = "不能为空白";

    public static final String ACCOUNT_NOT_FOUND = "账号不存在";
    public static final String BOOK_NOT_FOUND = "书籍不存在";
    public static final String BORROW_NOT_FOUND = "书籍借阅记录不存在";
    //public static final String CATEGORY_NOT_FOUND = "书籍类别不存在";
    public static final String FIELD_NOT_FOUND = "筛选字段不存在";
    public static final String CAPTCHA_NOT_FOUND = "验证码已过期，请重新获取";

    public static final String EMAIL_ALREADY_EXISTS = "邮箱已存在";
    public static final String CATEGORY_ALREADY_EXISTS = "书籍类别已存在";
    public static final String ISBN_ALREADY_EXISTS = "ISBN已存在";
    public static final String ACCOUNT_ALREADY_EXISTS = "账号已存在";
    public static final String PHONE_ALREADY_EXISTS = "电话号码已存在";

    public static final String INVALID_ISBN = "ISBN应为978或979开头的13位数字";
    public static final String INVALID_PHONE = "电话号码应为以1开头的11位数字，并且第二位数字不能为012";
    public static final String INVALID_EMAIL = "电子邮箱格式不正确";
    public static final String INVALID_ADMIN_NAME = "管理员名称应为2~16位，并且不含空白字符";
    public static final String INVALID_USER_NAME = "用户名应为2~16位，并且不含空白字符";
    public static final String INVALID_GENDER = "性别应为'男'或'女'";
    public static final String INVALID_ACCOUNT = "账号应为4~16位，并且只能包含字母、数字、下划线";
    public static final String INVALID_PASSWORD = "密码应为4~16位，并且只能包含字母、数字、下划线";
    public static final String INVALID_RESERVE_DATE = "预定日期必须是一个将来的日期";
    public static final String INVALID_RETURN_DATE = "归还日期必须是一个将来的日期";
    public static final String INVALID_BOOK_NAME = "书名中不能出现除了空格以外的空白字符；不能出现连续的空格；不能以空白字符开头或结尾";
    public static final String INVALID_AUTHOR_NAME = "作者名称中不能出现除了空格以外的空白字符；不能出现连续的空格；不能以空白字符开头或结尾";
    public static final String INVALID_PUBLISHER = "出版社名称中不能出现除了空格以外的空白字符；不能出现连续的空格；不能以空白字符开头或结尾";
    public static final String INVALID_CATEGORY_NAME = "书籍类别名应为2~16位，并且不含空白字符";
    public static final String RETURN_DATE_BEFORE_RESERVATION = "归还日期不能早于预定日期";

    public static final String UPLOAD_FAILED = "文件上传失败";
    public static final String CREATE_CAPTCHA_FAILED = "获取图形验证码失败";
    public static final String PASSWORD_ERROR = "密码错误";
    public static final String CAPTCHA_ERROR = "验证码错误";

    public static final String CANCELLATION_IS_NOT_ALLOWED = "状态不为“已预约”的借阅记录不允许进行取消操作";
    public static final String DELETION_IS_NOT_ALLOWED = "状态不为“已取消”或“已归还”的借阅记录不允许进行删除操作";
    public static final String BOOK_REFERENCES_CATEGORY = "不能删除，有书籍信息关联了当前类别";
    public static final String BOOK_STOCK_NOT_ENOUGH = "不能删除，该书籍当前库存不足";
    public static final String MISSING_UPDATE_VALUE = "没有填写更改值";
    public static final String ACCOUNT_LOCKED = "当前账号被锁定";
    //public static final String NO_ACCESS = "无权访问";

}
