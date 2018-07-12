@file:JvmName("PrefHelper")

package com.example.astrodashalib.helper

import android.annotation.SuppressLint
import android.content.Context
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

fun set(key: String, value: Any,context: Context) {

    val edit = getDefaultSharedPreferences(context).edit()
    when (value) {
        is String -> edit.putString(key, value)
        is Int -> edit.putInt(key, value)
        is Long -> edit.putLong(key, value)
        is Boolean -> edit.putBoolean(key, value)
        is Float -> edit.putFloat(key, value)
    }
    edit.commit()//apply on UI
}

fun getString(key: String,context: Context): String? {
    return getDefaultSharedPreferences(context).getString(key, null)
}

fun getString(key: String, defaultValue: String?,context: Context): String? {
    return getDefaultSharedPreferences(context).getString(key, defaultValue)
}

fun getBoolean(key: String,context: Context): Boolean {
    return getDefaultSharedPreferences(context).getBoolean(key, false)
}

fun getInt(key: String,context: Context): Int {
    return getDefaultSharedPreferences(context).getInt(key, 0)
}

fun getInt(key: String, defaultValue: Int,context: Context): Int {
    return getDefaultSharedPreferences(context).getInt(key, defaultValue)
}

fun getLong(key: String,context: Context): Long {
    return getDefaultSharedPreferences(context).getLong(key, 0)
}

fun getFloat(key: String,context: Context): Float {
    return getDefaultSharedPreferences(context).getFloat(key, 0f)
}

fun clearKey(key: String,context: Context) {
    getDefaultSharedPreferences(context).edit().remove(key).apply()
}

fun isExist(key: String,context: Context): Boolean {
    return getDefaultSharedPreferences(context).contains(key)
}

fun clearPrefs(context: Context) {
    getDefaultSharedPreferences(context).edit().clear().apply()
}

fun remove(key: String,context: Context): Boolean {
    val edit = PreferenceManager.getDefaultSharedPreferences(context).edit()
    return edit.remove(key).commit()
}

fun getAll(context: Context): Map<String, *> {
    return getDefaultSharedPreferences(context).getAll()
}