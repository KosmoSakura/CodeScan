package project.zero.codescan.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

import java.util.Map;
import java.util.Set;

/**
 * @author hxd
 * @ClassName: PreferencesService
 * @Description: 保存临时信息
 * @date 2015-8-17 - 上午9:59:34
 */
public class PreferencesService {
    private static SharedPreferences mPreferences;
    private static Context mContext;
    private static String mShareName = "CodeScan";

    private PreferencesService() {
    }

    private static PreferencesService mPreferencesService;

    public static void init(Context context) {
        if (mPreferencesService == null) {
            mContext = context;
            mPreferencesService = new PreferencesService();
            mPreferences = mContext.getSharedPreferences(mShareName, Context.MODE_PRIVATE);
        }
    }

    /**
     * 批量保存
     */
    public static void saveInfo(Map<String, String> info) {
        Editor editor = mPreferences.edit();
        Set<String> keys = info.keySet();
        for (String str : keys) {
            editor.putString(str, info.get(str));
        }
        editor.commit();
    }

    /**
     * 保存string类型数据
     */
    public static void setPreferences(String name, String value) {
        Editor editor = mPreferences.edit();
        editor.putString(name, value);
        editor.commit();
    }

    /**
     * 保存long类型数据
     */
    public static void setLong(String name, long value) {
        Editor editor = mPreferences.edit();
        editor.putLong(name, value);
        editor.commit();
    }

    /**
     * 保存long类型数据
     */
    public static void setBoolean(String name, Boolean value) {
        Editor editor = mPreferences.edit();
        editor.putBoolean(name, value);
        editor.commit();
    }

    public static String getCity() { // NO_UCD (unused code)
        return mPreferences.getString("city_name", "");
    }

    /**
     * 保存long类型数据
     */
    public static long getLong(String name) { // NO_UCD (unused code)
        return mPreferences.getLong(name, -1);
    }

    public static void setInt(String name, int value) {
        Editor editor = mPreferences.edit();
        editor.putInt(name, value);
        editor.commit();
    }

    public static void setFloat(String name, float value) {
        Editor editor = mPreferences.edit();
        editor.putFloat(name, value);
        editor.commit();
    }

    /**
     * 保存int类型数据
     */
    public static int getInt(String name) {
        return mPreferences.getInt(name, 0);
    }

    /**
     * 保存int类型数据
     */
    public static float getFloat(String name) {
        return mPreferences.getFloat(name, 0);
    }

    /**
     * 根据key读取参数
     */
    public static String getInfo(String key) {
        return mPreferences.getString(key, "");
    }

    /**
     * 根据key读取参数,带默认值
     */
    public static String getInfo(String key, String def) {
        return mPreferences.getString(key, def);
    }

    /**
     * 读取状�?
     */
    public static boolean getBoolean(String key, boolean def) {
        return mPreferences.getBoolean(key, def);
    }

    public static void delete(String key) {
        Editor editor = mPreferences.edit();
        editor.remove(key);
        editor.commit();
    }

    /**
     * 读取状�?
     */
    public static void putBoolean(String key, boolean val) {
        Editor editor = mPreferences.edit();
        editor.putBoolean(key, val);
        editor.commit();
    }

    public static void clear() {
        Editor editor = mPreferences.edit();
        editor.clear();
        editor.commit();
    }

}
