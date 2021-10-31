package lab.maxb.dark.Presentation.View

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.ActivityResultRegistry
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.StringRes
import androidx.fragment.app.Fragment
import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.LifecycleOwner
import androidx.navigation.fragment.findNavController
import lab.maxb.dark.Presentation.Extra.Delegates.autoCleaned
import lab.maxb.dark.Presentation.Extra.FragmentKeys
import lab.maxb.dark.Presentation.Repository.Network.OAUTH.Google.GoogleSignInLogic
import lab.maxb.dark.Presentation.ViewModel.LoginViewModel
import lab.maxb.dark.R
import lab.maxb.dark.databinding.LoginFragmentBinding
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.viewModel


class LoginFragment : Fragment() {
    private val mViewModel: LoginViewModel by viewModel()
    private var mBinding: LoginFragmentBinding by autoCleaned()
    lateinit var mGoogleSignInPage : GoogleSignInPage
    val mGoogleSignInLogic by inject<GoogleSignInLogic>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mGoogleSignInLogic.buildClient(requireActivity())
        mGoogleSignInPage = GoogleSignInPage(
            requireActivity().activityResultRegistry,
            ::onAuthWithGoogle
        )
        lifecycle.addObserver(mGoogleSignInPage)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        mBinding = LoginFragmentBinding.inflate(layoutInflater, container, false)
        changeLoginButtonsIsEnable(false)
        mGoogleSignInLogic.getAuthCode(requireActivity()).observe(viewLifecycleOwner) { authCode ->
            mViewModel.authorizeBySession(authCode).observe(viewLifecycleOwner) { profile ->
                if (profile != null)
                    onAuthorized()
                else
                    onNotAuthorized(null)
            }
        }
        mBinding.signIn.setOnClickListener {
            changeLoginButtonsIsEnable(false)
            mViewModel.authorize(
                mBinding.login.text.toString(),
                mBinding.password.text.toString(),
            ).observe(viewLifecycleOwner) { profile ->
                if (profile != null)
                    onAuthorized()
                else
                    onNotAuthorized(R.string.incorrect_credentials_message)
            }
        }

        mBinding.googleSignIn.setOnClickListener {
            changeLoginButtonsIsEnable(false)
            mGoogleSignInPage.open(mGoogleSignInLogic.signInIntent)
        }
        return mBinding.root
    }

    private fun onAuthWithGoogle(result: Intent?){
        val credentials = result?.let { mGoogleSignInLogic.handleSignInResult(it) }
        if (credentials == null)
            onNotAuthorized(R.string.something_went_wrong,)
        else {
            mViewModel.authorizeByOAUTHProvider(
                credentials[0],
                credentials[1],
                credentials[2],
            ).observe(viewLifecycleOwner) { profile ->
                if (profile != null)
                    onAuthorized()
                else
                    onNotAuthorized(R.string.something_went_wrong,)
            }
        }
    }

    private fun onNotAuthorized(@StringRes message: Int?) {
        message?.let { Toast.makeText(
            context, it, Toast.LENGTH_LONG
        ).show()}
        changeLoginButtonsIsEnable(true)
    }

    fun changeLoginButtonsIsEnable(isEnabled: Boolean) {
        mBinding.googleSignIn.isEnabled = isEnabled
        mBinding.signIn.isEnabled = isEnabled
    }

    private fun onAuthorized() {
        mBinding.password.setText("")
        findNavController().navigate(
            LoginFragmentDirections.toRecognitionTaskListFragment(),
        )
    }

    class GoogleSignInPage(
        private val registry : ActivityResultRegistry,
        private val onResultListener : (Intent?) -> Unit,
    ): DefaultLifecycleObserver {
        lateinit var getContent : ActivityResultLauncher<Intent>

        override fun onCreate(owner: LifecycleOwner) {
            getContent = registry.register(GET_GOOGLE_ACCOUNT, owner,
                ActivityResultContracts.StartActivityForResult()) {
                onResultListener(it.data)
            }
        }

        override fun onDestroy(owner: LifecycleOwner) {
            getContent.unregister()
            super.onDestroy(owner)
        }

        fun open(intent: Intent) {
            getContent.launch(intent)
        }
    }

    companion object {
        private val keys = FragmentKeys(this::class)
        val GET_GOOGLE_ACCOUNT by keys.special()
    }
}