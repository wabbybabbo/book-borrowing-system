package org.example.client.context;

public class UserContext {

    private static final ThreadLocal<Long> tl = new ThreadLocal<>();

    /**
     * 保存当前登录用户id到ThreadLocal中
     *
     * @param userId 用户id
     */
    public static void setUserId(long userId) {
        tl.set(userId);
    }

    /**
     * 从ThreadLocal中获取当前登录的用户id
     */
    public static long getUserId() {
        return tl.get();
    }

    /**
     * 移除ThreadLocal中当前登录的用户id
     */
    public static void removeUserId() {
        tl.remove();
    }

}
