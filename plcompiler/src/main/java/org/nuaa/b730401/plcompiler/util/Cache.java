package org.nuaa.b730401.plcompiler.util;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @Author: ToMax
 * @Description:
 * @Date: Created in 2019/1/8 21:36
 */
public class Cache {
    private static Map<String, Object> cacheMap = new ConcurrentHashMap<>();

    public static void setAttribute(String key, Object value) {
        cacheMap.put(key, value);
    }

    public static Object getAttribute(String key) {
        return cacheMap.containsKey(key) ? cacheMap.get(key) : null;
    }

    public static void removeAttribute(String key) {
        cacheMap.remove(key);
    }
}
