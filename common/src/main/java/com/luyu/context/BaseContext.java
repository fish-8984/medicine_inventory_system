package com.luyu.context;

public class BaseContext {
    public static ThreadLocal<Long> targetIdHolder = new ThreadLocal<>();
    public static ThreadLocal<String> targetIdHolderString = new ThreadLocal<>();

    /**
     * 设置当前操作对象ID
     * @param targetId
     */
    public static void setTargetId(Object targetId) {
        if (targetId == null) {
            return;
        }
        if (targetId.getClass().equals(Long.class)) {
            targetIdHolder.set((Long) targetId);
        }
        if (targetId.getClass().equals(Integer.class)) {
            targetIdHolder.set(((Integer) targetId).longValue());
        }
    }
    public static Long getTargetId() {
        return targetIdHolder.get();
    }
    public static int getTargetIdAsInt() {
        return targetIdHolder.get().intValue();
    }
    public static void removeTargetId() {
        targetIdHolder.remove();
    }


    public static void setTargetIdString(String targetId) {
        if (targetId == null) {
            return;
        }
        targetIdHolderString.set(targetId);
    }
    public static String getTargetIdString() {
        return targetIdHolderString.get();
    }
    public static void removeTargetIdString() {
        targetIdHolderString.remove();
    }
}
