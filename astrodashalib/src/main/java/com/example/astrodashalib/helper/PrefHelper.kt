@file:JvmName("PrefHelper")

package com.example.astrodashalib.helper

import android.annotation.SuppressLint
import android.preference.PreferenceManager
import android.preference.PreferenceManager.getDefaultSharedPreferences

/**
 * Created by himanshu on 25/09/17.
 */


/**
 * @param key   ( the Key to used to retrieve this data later  )
 * @param value ( any kind of primitive values  )
 *
 *
 * non can be null!!!
 */
@SuppressLint("ApplySharedPref")

fun set(key: String, value: Any) {

    val edit = getDefaultSharedPreferences().edit()
    when (value) {
        is String -> edit.putString(key, value)
        is Int -> edit.putInt(key, value)
        is Long -> edit.putLong(key, value)
        is Boolean -> edit.putBoolean(key, value)
        is Float -> edit.putFloat(key, value)
    }
    edit.commit()//apply on UI
}

fun getString(key: String): String? {
    return getDefaultSharedPreferences(BaseApplication.getInstance()).getString(key, null)
}

fun getString(key: String, defaultValue: String): String? {
    return getDefaultSharedPreferences(BaseApplication.getInstance()).getString(key, defaultValue)
}

fun getBoolean(key: String): Boolean {
    return getDefaultSharedPreferences(BaseApplication.getInstance()).getBoolean(key, false)
}

fun getInt(key: String): Int {
    return getDefaultSharedPreferences(BaseApplication.getInstance()).getInt(key, 0)
}

fun getInt(key: String, defaultValue: Int): Int {
    return getDefaultSharedPreferences(BaseApplication.getInstance()).getInt(key, defaultValue)
}

fun getLong(key: String): Long {
    return getDefaultSharedPreferences(BaseApplication.getInstance()).getLong(key, 0)
}

fun getFloat(key: String): Float {
    return getDefaultSharedPreferences(BaseApplication.getInstance()).getFloat(key, 0f)
}

fun clearKey(key: String) {
    getDefaultSharedPreferences(BaseApplication.getInstance()).edit().remove(key).apply()
}

fun isExist(key: String): Boolean {
    return getDefaultSharedPreferences(BaseApplication.getInstance()).contains(key)
}

fun clearPrefs() {
    getDefaultSharedPreferences(BaseApplication.getInstance()).edit().clear().apply()
}

fun remove(key: String): Boolean {
    val edit = PreferenceManager.getDefaultSharedPreferences(BaseApplication.getInstance()).edit()
    return edit.remove(key).commit()
}

fun getAll(): Map<String, *> {
    return getDefaultSharedPreferences(BaseApplication.getInstance()).getAll()
}