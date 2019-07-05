package com.xiaochong.loan.background.utils;
import java.util.Collection;
import java.util.List;
import java.util.Map;

/**
 * java中集合类的工具类
 *
 * @author cx
 */
public class CollectionUtil {

    /**
     * map是否为null或者map为空
     *
     * @param map
     * @return
     */
    public static boolean isBlank(Map map) {
        if (null == map || map.isEmpty()) return true;
        return false;
    }

    /**
     * map不为null不为空
     *
     * @param map
     * @return
     */
    public static boolean isNotBlank(Map map) {
        if (null != map && !map.isEmpty()) return true;
        return !isBlank(map);
    }

    /**
     * collection是否为null 或者为空
     *
     * @param cols
     * @return
     */
    public static boolean isBlank(Collection<?> cols) {
        if (null == cols || cols.isEmpty()) return true;
        return false;
    }

    /**
     * collection不为null 不为空
     *
     * @param cols
     * @return
     */
    public static boolean isNotBlank(Collection<?> cols) {
        if(null != cols && !cols.isEmpty()) return true;
        return false;
    }

    /**
     * 获取指定list中的最后一个元素
     *
     * @param list
     * @param <E>
     * @return
     */
    public static <E> E getLast(List<E> list) {
        if (isNotBlank(list)) {
            return list.get(list.size() - 1);
        }
        return null;
    }

    /**
     * 获取指定list中的第一个元素
     *
     * @param list
     * @param <E>
     * @return
     */
    public static <E> E getFirst(List<E> list) {
        if (isNotBlank(list)) {
            return list.get(0);
        }
        return null;
    }


}

