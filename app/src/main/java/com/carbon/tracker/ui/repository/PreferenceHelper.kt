package com.carbon.tracker.ui.repository

interface PreferenceHelper {
    fun saveString(key: String, value: String)
    fun getString(key: String): String
    fun getString(key: String, defaultValue: String?): String

    fun saveBoolean(key: String, value: Boolean)
    fun getBoolean(key: String): Boolean
    fun getBoolean(key: String, defaultValue: Boolean): Boolean

    fun clear(key: String): Boolean
    fun contains(key: String): Boolean
}