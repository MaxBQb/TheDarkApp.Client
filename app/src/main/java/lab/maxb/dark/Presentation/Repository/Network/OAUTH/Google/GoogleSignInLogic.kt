package lab.maxb.dark.Presentation.Repository.Network.OAUTH.Google

import android.app.Activity
import android.content.Intent
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.android.gms.auth.api.signin.*
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.tasks.Task
import lab.maxb.dark.BuildConfig

class GoogleSignInLogic {
    private lateinit var mGoogleSignInClient: GoogleSignInClient

    lateinit var signInIntent: Intent
        private set

    fun getAuthCode(activity: Activity): LiveData<String?> {
        val liveData = MutableLiveData<String?>()
        val authCode = GoogleSignIn.getLastSignedInAccount(activity)?.serverAuthCode
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
            account.serverAuthCode!!
        )
    } catch (e: Throwable) {
        e.printStackTrace()
        null
    }

    companion object {
        private val options = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestServerAuthCode(BuildConfig.GOOGLE_CLIENT_ID, true)
            .requestEmail()
            .requestProfile()
            .build()
    }
}