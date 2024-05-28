package com.carbon.tracker.data.local.preferences

interface MainLocal {
    fun saveTheme(isDark: Boolean)
    fun getTheme(): Boolean
    fun saveOnboardState()
    fun containOnboardState(): Boolean
    fun saveUsername(username: String?)
    fun getUsername(): String
    fun deleteUsername(): Boolean
    fun saveEmail(email: String)
    fun getEmail(): String
    fun deleteEmail(): Boolean
    fun savePassword(password: String)
    fun getPassword(): String
    fun deletePassword(): Boolean
    fun containsLoginInfo(): Boolean
}