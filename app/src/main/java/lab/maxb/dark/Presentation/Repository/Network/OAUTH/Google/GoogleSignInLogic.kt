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
            liveData.postValue(handleSignIn(task)?.second)
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

    fun handleSignInResult(result: Intent): Pair<String, String>?
        = handleSignIn(GoogleSignIn.getSignedInAccountFromIntent(result))

    private fun handleSignIn(task: Task<GoogleSignInAccount>): Pair<String, String>? = try {
        val account = task.getResult(ApiException::class.java)
        account.email!! to account.serverAuthCode!!
    } catch (e: Throwable) {
        GoogleSignInStatusCodes.CONNECTION_SUSPENDED_DURING_CALL
        e.printStackTrace()
        null
    }

    companion object {
        private val options = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestServerAuthCode(BuildConfig.GOOGLE_CLIENT_ID, true)
            .requestEmail()
            .build()
    }
}