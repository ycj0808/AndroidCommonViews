package me.icefire.update.utils;


import android.content.Context;
import android.content.SharedPreferences;

/**
 * SharedPreferences工具类封装
 */
public class SpUtil {
    private static SharedPreferences sp;
    private static String mPreferencesName = "share_preference_default";

    /**
     * 设置preferencesName
     *
     * @param preferencesName preferencesName
     */
    private void setPreferencesName(String preferencesName) {
        mPreferencesName = preferencesName;
    }

    /**
     * 写入boolean变量至sp中
     *
     * @param ctx   上下文环境
     * @param key   存储节点名称
     * @param value 存储节点的值
     */
    public static void putBoolean(Context ctx, String key, boolean value) {
        //(存储节点文件名称,读写方式)
        if (sp == null) {
            sp = ctx.getSharedPreferences(mPreferencesName, Context
                    .MODE_PRIVATE);
        }
        sp.edit().putBoolean(key, value).apply();
    }

    /**
     * 读取boolean标示从sp中
     *
     * @param ctx      上下文环境
     * @param key      存储节点名称
     * @param defValue 没有此节点默认值
     * @return 默认值或者此节点读取到的结果
     */
    public static boolean getBoolean(Context ctx, String key, boolean defValue) {
        //(存储节点文件名称,读写方式)
        if (sp == null) {
            sp = ctx.getSharedPreferences(mPreferencesName, Context
                    .MODE_PRIVATE);
        }
        return sp.getBoolean(key, defValue);
    }

    /**
     * 写入String变量至sp中
     *
     * @param ctx   上下文环境
     * @param key   存储节点名称
     * @param value 存储节点的值
     */
    public static void putString(Context ctx, String key, String value) {
        //(存储节点文件名称,读写方式)
        if (sp == null) {
            sp = ctx.getSharedPreferences(mPreferencesName, Context
                    .MODE_PRIVATE);
        }
        sp.edit().putString(key, value).apply();
    }

    /**
     * 读取String标示从sp中
     *
     * @param ctx      上下文环境
     * @param key      存储节点名称
     * @param defValue 没有此节点默认值
     * @return 默认值或者此节点读取到的结果
     */
    public static String getString(Context ctx, String key, String defValue) {
        //(存储节点文件名称,读写方式)
        if (sp == null) {
            sp = ctx.getSharedPreferences(mPreferencesName, Context
                    .MODE_PRIVATE);
        }
        return sp.getString(key, defValue);
    }


    /**
     * 写入int变量至sp中
     *
     * @param ctx   上下文环境
     * @param key   存储节点名称
     * @param value 存储节点的值
     */
    public static void putInt(Context ctx, String key, int value) {
        //(存储节点文件名称,读写方式)
        if (sp == null) {
            sp = ctx.getSharedPreferences(mPreferencesName, Context
                    .MODE_PRIVATE);
        }
        sp.edit().putInt(key, value).apply();
    }

    public static void putLong(Context ctx, String key, long value) {
        //(存储节点文件名称,读写方式)
        if (sp == null) {
            sp = ctx.getSharedPreferences(mPreferencesName, Context
                    .MODE_PRIVATE);
        }
        sp.edit().putLong(key, value).apply();
    }

    /**
     * 读取int标示从sp中
     *
     * @param ctx      上下文环境
     * @param key      存储节点名称
     * @param defValue 没有此节点默认值
     * @return 默认值或者此节点读取到的结果
     */
    public static int getInt(Context ctx, String key, int defValue) {
        //(存储节点文件名称,读写方式)
        if (sp == null) {
            sp = ctx.getSharedPreferences(mPreferencesName, Context
                    .MODE_PRIVATE);
        }
        return sp.getInt(key, defValue);
    }

    public static long getLong(Context ctx, String key, long defValue){
        if (sp==null){
            sp=ctx.getSharedPreferences(mPreferencesName, Context
                    .MODE_PRIVATE);
        }
        return sp.getLong(key,defValue);
    }


    /**
     * 从sp中移除指定节点
     *
     * @param ctx 上下文环境
     * @param key 需要移除节点的名称
     */
    public static void remove(Context ctx, String key) {
        if (sp == null) {
            sp = ctx.getSharedPreferences(mPreferencesName, Context
                    .MODE_PRIVATE);
        }
        sp.edit().remove(key).apply();
    }
}
