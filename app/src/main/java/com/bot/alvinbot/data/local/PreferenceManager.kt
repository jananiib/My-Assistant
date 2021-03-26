package com.bot.alvinbot.data.local

import android.content.SharedPreferences
import androidx.core.content.edit
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import javax.inject.Inject


class PreferenceManager @Inject constructor(private val preference: SharedPreferences) {

    companion object {
        const val EMERGENCY_NUMBER = "EMERGENCY_NUMBER"
        const val USER_NAME = "USER_NAME"
    }

    /**
     * Set the String value in the shared preference with the given key.
     *
     * @param key     String used as a key for accessing the value.
     * @param value   String value which is to be stored in shared preference.
     */
    fun setValue(key: String, value: String) {
        preference.edit {
            putString(key, value)
        }
    }


    /**
     * Set the Int value in the shared preference with the given key.
     *
     * @param key     String used as a key for accessing the value.
     * @param value   String value which is to be stored in shared preference.
     */
    fun setValue(key: String, value: Int) {
        preference.edit {
            putInt(key, value)
        }
    }

    /**
     * Set the Boolean value in the shared preference with the given key.
     *
     * @param key     String used as a key for accessing the value.
     * @param value   String value which is to be stored in shared preference.
     */
    fun setValue(key: String, value: Boolean) {
        preference.edit {
            putBoolean(key, value)
        }
    }

    /**
     * Set the Float value in the shared preference with the given key.
     *
     * @param key     String used as a key for accessing the value.
     * @param value   String value which is to be stored in shared preference.
     */
    fun setValue(key: String, value: Float) {
        preference.edit {
            putFloat(key, value)
        }
    }

    /**
     * Returns String value for the given key.
     * By default it will return null.
     *
     * @param key String used as a key for accessing the value.
     * @return null by default; returns the String value for the given key.
     */
    fun getStringValue(key: String): String? {
        return preference.getString(key, "")
    }

    /**
     * Returns Integer value for the given key.
     * By default it will return "-1".
     *
     * @param key String used as a key for accessing the value.
     * @return -1 by default; returns the Integer value for the given key.
     */
    fun getIntValue(key: String): Int {
        return preference.getInt(key, -1)
    }

    /**
     * Returns Boolean value for the given key.
     * By default it will return "false".
     *
     * @param key String used as a key for accessing the value.
     * @return false by default; returns the Boolean value for the given key.
     */
    fun getBooleanValue(key: String): Boolean {
        return preference.getBoolean(key, false)
    }

    /**
     * Returns Float value for the given key.
     * By default it will return 0.0f.
     *
     * @param key String used as a key for accessing the value.
     * @return 0.0f by default; returns the Float value for the given key.
     */
    fun getFloatValue(key: String): Float {
        return preference.getFloat(key, 0.0f)
    }

    /*
    * save object
    *
    * */

    fun <T> setObject(key: String, value: T) {
        val json = Gson().toJson(value)
        setValue(key, json)
    }


    /*
      * read object
      *
      * */
    fun <T> getObject(key: String, className: Class<T>): T? {
        if (preference.contains(key)) {
            val json = getStringValue(key)
            return Gson().fromJson(json!!, className)
        } else {
            return null
        }
    }

    /**
     * Clear value in shared preference based on key
     */
    fun clearValue(key: String) {
        preference.edit().remove(key).apply()
    }

    /**
     * Clear all shared preference values
     */
    fun clearAllValues() {
        preference.edit().clear().apply()
    }
/*
    fun <T> setSosList(key: String?, list: List<T>?) {
        val gson = Gson()
        val json = gson.toJson(list)
        set(key, json)
    }

    fun set(key: String?, value: String?) {
        preference.edit {
            putString(key, value)
        }
    }*/



}