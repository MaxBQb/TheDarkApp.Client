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
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.fragment.app.setFragmentResult
import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.LifecycleOwner
import lab.maxb.dark.Domain.Model.Server.Profile
import lab.maxb.dark.MainActivity
import lab.maxb.dark.Presentation.Extra.Delegates.autoCleaned
import lab.maxb.dark.Presentation.Extra.FragmentKeys
import lab.maxb.dark.Presentation.Extra.observeOnce
import lab.maxb.dark.Presentation.Extra.toggleVisibility
import lab.maxb.dark.Presentation.Repository.Network.OAUTH.Google.GoogleSignInLogic
import lab.maxb.dark.Presentation.ViewModel.UserViewModel
import lab.maxb.dark.R
import lab.maxb.dark.databinding.LoginFragmentBinding
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.sharedViewModel


class LoginFragment : Fragment() {
    private val mViewModel: UserViewModel by sharedViewModel()
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
        return mBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mGoogleSignInLogic.getAuthCode(requireActivity()).observeOnce(viewLifecycleOwner) { authCode ->
            mViewModel.authorizeBySession(authCode).observeOnce(viewLifecycleOwner,
                makeAuthResultHandler(null))
        }
        mBinding.signIn.setOnClickListener {
            changeLoginButtonsIsEnable(false)
            mViewModel.authorize(
                mBinding.login.text.toString(),
                mBinding.password.text.toString(),
            ).observeOnce(
                viewLifecycleOwner,
                makeAuthResultHandler(R.string.incorrect_credentials_message)
            )
        }

        mBinding.googleSignIn.setOnClickListener {
            changeLoginButtonsIsEnable(false)
            mGoogleSignInPage.open(mGoogleSignInLogic.signInIntent)
        }
        toggleToolbarVisibility(false)
    }

    private fun toggleToolbarVisibility(isVisible: Boolean)
        = (requireActivity() as MainActivity).binding
            .toolbar.toggleVisibility(isVisible)

    private fun onAuthWithGoogle(result: Intent?){
        val credentials = result?.let { mGoogleSignInLogic.handleSignInResult(it) }
        if (credentials == null)
            onNotAuthorized(R.string.something_went_wrong)
        else {
            mViewModel.authorizeByOAUTHProvider(
                credentials[0],
                credentials[1],
                credentials[2],
            ).observeOnce(
                viewLifecycleOwner,
                makeAuthResultHandler(R.string.something_went_wrong)
            )
        }
    }

    private fun makeAuthResultHandler(@StringRes message: Int?) = { profile: Profile? ->
        if (profile != null) {
            mBinding.password.setText("")
            setFragmentResult(RESPONSE_LOGIN_SUCCESSFUL, bundleOf())
            requireActivity().onBackPressed()
            toggleToolbarVisibility(true)
        } else onNotAuthorized(message)
    }

    private fun onNotAuthorized(@StringRes message: Int?) {
        message?.let { Toast.makeText(
            context, it, Toast.LENGTH_LONG
        ).show()}
        changeLoginButtonsIsEnable(true)
    }

    private fun changeLoginButtonsIsEnable(isEnabled: Boolean) {
        mBinding.googleSignIn.isEnabled = isEnabled
        mBinding.signIn.isEnabled = isEnabled
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
        val RESPONSE_LOGIN_SUCCESSFUL by keys.communication()
    }
}