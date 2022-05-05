package lab.maxb.dark.presentation.repository.network.oauth.google

import android.app.Activity
import android.content.Intent
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.tasks.Task
import lab.maxb.dark.BuildConfig
import org.koin.core.annotation.Single

@Single
class GoogleSignInLogic {
    private lateinit var mGoogleSignInClient: GoogleSignInClient

    lateinit var signInIntent: Intent
        private set

    fun getAuthCode(activity: Activity): LiveData<String?> {
        val liveData = MutableLiveData<String?>()
        val authCode = GoogleSignIn.getLastSignedInAccount(activity)?.idToken
        if (authCode != null) {
            liveData.postValue(authCode)
            return liveData
        }
        mGoogleSignInClient.silentSignIn().addOnCompleteListener(activity) { task ->
            liveData.postValue(handleSignIn(task)?.last())
        }.addOnCanceledListener(activity) {
            liveData.postValue(null)
        }

        return liveData
    }

    fun buildClient(activity: Activity) {
        mGoogleSignInClient = GoogleSignIn.getClient(activity, options)
        signInIntent = mGoogleSignInClient.signInIntent
    }

    fun signOut() = mGoogleSignInClient.signOut()

    fun handleSignInResult(result: Intent)
        = handleSignIn(GoogleSignIn.getSignedInAccountFromIntent(result))

    private fun handleSignIn(task: Task<GoogleSignInAccount>) = try {
        val account = task.getResult(ApiException::class.java)
        arrayOf(
            account.email!!,
            account.displayName!!,
            account.idToken!!
        )
    } catch (e: Throwable) {
        println(e.localizedMessage)
        null
    }

    companion object {
        private val options = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(BuildConfig.GOOGLE_CLIENT_ID)
            .requestEmail()
            .requestProfile()
            .build()
    }
}