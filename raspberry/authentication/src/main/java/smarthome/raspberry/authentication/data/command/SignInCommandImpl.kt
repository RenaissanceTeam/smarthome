package smarthome.raspberry.authentication.data.command

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import smarthome.raspberry.authentication.api.domain.Credentials
import smarthome.raspberry.authentication.api.domain.User
import smarthome.raspberry.authentication.data.mapper.CredentialsToAuthCredentialsMapper
import smarthome.raspberry.authentication.data.mapper.FirebaseUserToUserMapper
import kotlin.coroutines.resumeWithException
import kotlin.coroutines.suspendCoroutine

class SignInCommandImpl(
    private val auth: FirebaseAuth,
    private val firebaseUserToUserMapper: FirebaseUserToUserMapper,
    private val credentialsToAuthCredentialsMapper: CredentialsToAuthCredentialsMapper
) : SignInCommand {
    
    override suspend fun execute(credential: Credentials): User {
        val firebaseUser =
            suspendCoroutine<FirebaseUser> { continuation ->
        
                val firebaseCredentials = credentialsToAuthCredentialsMapper.map(credential)
        
                auth.signInWithCredential(firebaseCredentials).addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        task.result?.user?.let {
                            continuation.resumeWith(Result.success(it))
                        } ?: continuation.resumeWithException(Throwable("user == null"))
                    } else {
                        continuation.resumeWithException(task.exception ?: Throwable())
                    }
                }
            }
        
        return firebaseUserToUserMapper.map(firebaseUser)
    }
}