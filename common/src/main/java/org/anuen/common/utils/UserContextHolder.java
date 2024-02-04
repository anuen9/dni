package org.anuen.common.utils;

public final class UserContextHolder {

    public static final ThreadLocal<String> tl = new ThreadLocal<>();

    private UserContextHolder() {
    }

    public static void setUser(String currentSysUser) {
        tl.set(currentSysUser);
    }

    public static String getUser() {
        return tl.get();
    }

    public static void removeUser() {
        tl.remove();
    }

}
