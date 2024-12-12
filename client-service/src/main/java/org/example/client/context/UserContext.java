package org.example.client.context;

public class UserContext {
    private static final ThreadLocal<Integer> tl = new ThreadLocal<>();

    /**
     * 保存当前登录用户id到ThreadLocal
     *
     * @param userId 用户id
     */
    public static void setUserId(int userId) {
        tl.set(userId);
    }

    /**
     * 获取当前登录用户id
     */
    public static int getUserId() {
        return tl.get();
    }

    /**
     * 移除当前登录用户id
     */
    public static void removeUserId() {
        tl.remove();
    }
}
