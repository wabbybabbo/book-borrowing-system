package org.example.common.constant;

/**
 * 信息提示常量类
 */
public class MessageConstant {

    public static final String FIELD_NOT_NULL = "不能为空";
    public static final String FIELD_NOT_EMPTY = "不能为空";
    public static final String FIELD_NOT_BLANK = "不能为空白";

    public static final String INVALID_ISBN = "无效的ISBN，ISBN应为13位";
    public static final String INVALID_PHONE = "无效的电话号码，电话号码应为11位";
    public static final String INVALID_EMAIL = "无效的电子邮箱";
    public static final String INVALID_ADMIN_NAME = "管理员名称应为2~16位，并且不含空白字符";
    public static final String INVALID_USER_NAME = "用户名应为2~16位，并且不含空白字符";
    public static final String INVALID_GENDER = "无效的性别，性别应为'男'或'女'";
    public static final String INVALID_ACCOUNT = "账号应为4~16位，并且只能包含字母、数字、下划线";
    public static final String INVALID_PASSWORD = "密码应为4~16位，并且只能包含字母、数字、下划线";
    public static final String INVALID_RESERVE_DATE = "预定日期必须是一个将来的日期";
    public static final String INVALID_RETURN_DATE = "归还日期必须是一个将来的日期";
    public static final String RETURN_DATE_BEFORE_RESERVATION = "归还日期不能早于预定日期";

    public static final String ACCOUNT_NOT_FOUND = "账号不存在";
    public static final String BOOK_NOT_FOUND = "书籍不存在";
    public static final String BORROW_NOT_FOUND = "书籍借阅记录不存在";
    public static final String CATEGORY_NOT_FOUND = "书籍类别不存在";
    public static final String FIELD_NOT_FOUND = "筛选字段不存在";

    public static final String CATEGORY_ALREADY_EXISTS = "书籍类别已存在";
    public static final String ISBN_ALREADY_EXISTS = "ISBN已存在";
    public static final String USERNAME_ALREADY_EXISTS = "用户名已存在";
    public static final String PHONE_ALREADY_EXISTS = "电话号码已存在";
    public static final String EMAIL_ALREADY_EXISTS = "邮箱已存在";

    public static final String LOGIN_SUCCESS = "登录成功";
    public static final String REGISTER_SUCCESS = "注册成功";
    public static final String UPDATE_SUCCESS = "更改成功";
    public static final String BORROW_SUCCESS = "借阅成功";
    public static final String CANCEL_SUCCESS = "取消成功";
    public static final String DELETE_SUCCESS = "删除成功";
    public static final String CREATE_SUCCESS = "新建成功";
    public static final String DISABLE_SUCCESS = "禁用成功";
    public static final String ENABLE_SUCCESS = "启用成功";

    public static final String UPLOAD_FAILED = "文件上传失败";
    public static final String PASSWORD_ERROR = "密码错误";

    public static final String CANCELLATION_IS_NOT_ALLOWED = "状态不为“已预约”的借阅记录不允许进行取消操作";
    public static final String DELETION_IS_NOT_ALLOWED = "状态不为“已取消”或“已归还”的借阅记录不允许进行删除操作";

    public static final String CATEGORY_BE_RELATED_BY_BOOK = "当前类别关联了书籍,不能删除";
    public static final String BOOK_STOCK_NOT_ENOUGH = "该书籍当前库存不足";
    public static final String UPDATE_FIELD_NOT_SET = "没有设置更新字段";
    public static final String ACCOUNT_LOCKED = "账号被锁定";
    public static final String NO_ACCESS = "无权访问";

}
