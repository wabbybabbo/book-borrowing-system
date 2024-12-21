//package org.example.admin.context;
//
//public class AdminContext {
//
//    private static final ThreadLocal<Long> tl = new ThreadLocal<>();
//
//    /**
//     * 保存当前登录管理员id到ThreadLocal中
//     *
//     * @param AdminId 管理员id
//     */
//    public static void setAdminId(long AdminId) {
//        tl.set(AdminId);
//    }
//
//    /**
//     * 从ThreadLocal中获取当前登录的管理员id
//     */
//    public static Long getAdminId() {
//        return tl.get();
//    }
//
//    /**
//     * 移除ThreadLocal中当前登录的管理员id
//     */
//    public static void removeAdminId() {
//        tl.remove();
//    }
//
//}
