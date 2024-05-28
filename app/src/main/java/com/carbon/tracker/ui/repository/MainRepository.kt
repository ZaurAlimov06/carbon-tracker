package com.carbon.tracker.ui.repository

import com.carbon.tracker.data.remote.dto.Consumption
import com.carbon.tracker.ui.model.Response
import com.google.firebase.auth.AuthResult
import kotlinx.coroutines.flow.Flow

interface MainRepository {
    suspend fun loginUser(email: String, password: String): Response<AuthResult>
    suspend fun registerUser(
        userName: String,
        email: String,
        password: String
    ): Response<AuthResult>

    suspend fun updateUser(userName: String): Response<Unit>
    suspend fun insertConsumption(consumption: Consumption): Flow<Response<Unit>>
    suspend fun getAllConsumption(): Flow<Response<List<Consumption>>>
    suspend fun logoutUser(): Response<Unit>

    fun saveUsername(username: String?)
    fun getUsername(): String
    fun saveEmail(email: String)
    fun getEmail(): String
    fun savePassword(password: String)
    fun getPassword(): String
    fun saveTheme(isDark: Boolean)
    fun getTheme(): Boolean
    fun saveOnboardState()
    fun containOnboardState(): Boolean
    fun containsLoginInfo(): Boolean
}