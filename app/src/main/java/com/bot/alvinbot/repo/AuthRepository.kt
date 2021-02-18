package com.bot.alvinbot.repo

import com.bot.alvinbot.data.network.Resource
import com.bot.alvinbot.data.network.Status
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class AuthRepository @Inject constructor(/*private val auth: FirebaseAuth*/) {

    suspend fun login(emailId: String, password: String): Flow<Resource<AuthResult>> {
        return flow {
           /* val result = auth.signInWithEmailAndPassword(
                emailId, password
            ).await()*/
            emit(Resource(Status.SUCCESS, null, null))
        }.catch {
            emit(Resource(Status.ERROR, null, it.message))
        }
    }

    suspend fun register(emailId: String, password: String): Flow<Resource<AuthResult>> {
        return flow {
          /*  val result = auth.createUserWithEmailAndPassword(
                emailId, password
            ).await()*/
            emit(Resource(Status.SUCCESS, null, null))
        }.catch {
            emit(Resource(Status.ERROR, null, it.message))
        }
    }


}