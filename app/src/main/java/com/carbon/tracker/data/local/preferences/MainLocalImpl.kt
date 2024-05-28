package com.carbon.tracker.data.local.preferences

import com.carbon.tracker.ui.repository.PreferenceHelper

class MainLocalImpl(
    private val preferenceHelper: PreferenceHelper
) : MainLocal {
    override fun saveTheme(isDark: Boolean) {
        preferenceHelper.saveBoolean(KEY_THEME, isDark)
    }

    override fun getTheme(): Boolean {
        return preferenceHelper.getBoolean(KEY_THEME)
    }

    override fun saveOnboardState() {
        preferenceHelper.saveBoolean(KEY_ONBOARD_STATE, true)
    }

    override fun containOnboardState(): Boolean {
        return preferenceHelper.contains(KEY_ONBOARD_STATE)
    }

    override fun saveUsername(username: String?) {
        preferenceHelper.saveString(KEY_USERNAME, username ?: "")
    }

    override fun getUsername(): String {
        return preferenceHelper.getString(KEY_USERNAME)
    }

    override fun deleteUsername(): Boolean {
        return preferenceHelper.clear(KEY_USERNAME)
    }

    override fun saveEmail(email: String) {
        preferenceHelper.saveString(KEY_EMAIL, email)
    }

    override fun getEmail(): String {
        return preferenceHelper.getString(KEY_EMAIL)
    }

    override fun deleteEmail(): Boolean {
        return preferenceHelper.clear(KEY_EMAIL)
    }

    override fun savePassword(password: String) {
        preferenceHelper.saveString(KEY_PASSWORD, password)
    }

    override fun getPassword(): String {
        return preferenceHelper.getString(KEY_PASSWORD)
    }

    override fun deletePassword(): Boolean {
        return preferenceHelper.clear(KEY_PASSWORD)
    }

    override fun containsLoginInfo(): Boolean {
        return preferenceHelper.contains(KEY_EMAIL) && preferenceHelper.contains(KEY_PASSWORD)
    }

    companion object {
        const val KEY_ONBOARD_STATE = "onboard.onboard_state"
        const val KEY_THEME = "profile.theme"
        const val KEY_USERNAME = "profile.username"
        const val KEY_EMAIL = "profile.email"
        const val KEY_PASSWORD = "profile.password"
    }
}