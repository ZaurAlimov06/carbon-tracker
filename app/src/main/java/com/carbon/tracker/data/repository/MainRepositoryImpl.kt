package com.carbon.tracker.data.repository

import com.carbon.tracker.data.local.preferences.MainLocal
import com.carbon.tracker.data.remote.dto.Consumption
import com.carbon.tracker.ui.model.Response
import com.carbon.tracker.ui.repository.MainRepository
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.UserProfileChangeRequest
import com.google.firebase.database.DatabaseReference
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.tasks.await

class MainRepositoryImpl(
    private val firebaseAuth: FirebaseAuth,
    private val databaseReference: DatabaseReference,
    private val mainLocal: MainLocal
) : MainRepository {
    override suspend fun loginUser(email: String, password: String): Response<AuthResult> {
        return try {
            val response = firebaseAuth.signInWithEmailAndPassword(email, password).await()

            Response.Success(response)
        } catch (e: Exception) {
            Response.Fail(e)
        }
    }

    override suspend fun registerUser(
        userName: String,
        email: String,
        password: String
    ): Response<AuthResult> {
        return try {
            val response = firebaseAuth.createUserWithEmailAndPassword(email, password).await()
            response.user?.updateProfile(
                UserProfileChangeRequest.Builder().setDisplayName(userName).build()
            )?.await()

            Response.Success(response)
        } catch (e: Exception) {
            Response.Fail(e)
        }
    }

    override suspend fun updateUser(userName: String): Response<Unit> {
        return try {
            val user = firebaseAuth.currentUser
            user?.updateProfile(UserProfileChangeRequest.Builder().setDisplayName(userName).build())
                ?.await()

            mainLocal.saveUsername(user?.displayName)

            Response.Success(Unit)
        } catch (e: Exception) {
            Response.Fail(e)
        }
    }

    override suspend fun insertConsumption(consumption: Consumption): Flow<Response<Unit>> {
        return flow {
            try {
                val userId = firebaseAuth.currentUser?.uid

                userId?.let {
                    databaseReference.child(userId)
                        .child(consumption.date)
                        .setValue(consumption)
                        .await()
                    emit(Response.Success(Unit))
                }
            } catch (ex: Exception) {
                emit(Response.Fail(ex))
            }
        }.onStart {
            emit(Response.Loading)
        }.catch {
            emit(Response.Fail(it))
        }.flowOn(Dispatchers.IO)
    }

    override suspend fun getAllConsumption(): Flow<Response<List<Consumption>>> {
        return flow {
            try {
                val userId = firebaseAuth.currentUser?.uid

                userId?.let {
                    val response = databaseReference.child(userId).get().await()

                    val responseList = mutableListOf<Consumption>()

                    if (response.exists()) {
                        for (recipeSnapshot in response.children) {
                            val consumption = recipeSnapshot.getValue(Consumption::class.java)
                            consumption?.let {
                                responseList.add(consumption)
                            }
                        }
                    }

                    emit(Response.Success(responseList))
                }
            } catch (ex: Exception) {
                emit(Response.Fail(ex))
            }
        }.onStart {
            emit(Response.Loading)
        }.catch {
            emit(Response.Fail(it))
        }.flowOn(Dispatchers.IO)
    }

    override suspend fun logoutUser(): Response<Unit> {
        return try {
            firebaseAuth.signOut()

            mainLocal.deleteUsername()
            mainLocal.deleteEmail()
            mainLocal.deletePassword()

            Response.Success(Unit)
        } catch (e: Exception) {
            Response.Fail(e)
        }
    }

    override fun saveUsername(username: String?) {
        mainLocal.saveUsername(username)
    }

    override fun getUsername(): String {
        return mainLocal.getUsername()
    }

    override fun saveEmail(email: String) {
        mainLocal.saveEmail(email)
    }

    override fun getEmail(): String {
        return mainLocal.getEmail()
    }

    override fun savePassword(password: String) {
        mainLocal.savePassword(password)
    }

    override fun getPassword(): String {
        return mainLocal.getPassword()
    }

    override fun saveTheme(isDark: Boolean) {
        mainLocal.saveTheme(isDark)
    }

    override fun getTheme(): Boolean {
        return mainLocal.getTheme()
    }

    override fun saveOnboardState() {
        mainLocal.saveOnboardState()
    }

    override fun containOnboardState(): Boolean {
        return mainLocal.containOnboardState()
    }

    override fun containsLoginInfo(): Boolean {
        return mainLocal.containsLoginInfo()
    }
}