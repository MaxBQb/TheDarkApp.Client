package lab.maxb.dark.presentation.view

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.ActivityResultRegistry
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.StringRes
import androidx.fragment.app.Fragment
import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.LifecycleOwner
import com.wada811.databinding.dataBinding
import kotlinx.coroutines.flow.collectLatest
import lab.maxb.dark.NavGraphDirections
import lab.maxb.dark.R
import lab.maxb.dark.databinding.LoginFragmentBinding
import lab.maxb.dark.domain.model.Profile
import lab.maxb.dark.domain.operations.unicname
import lab.maxb.dark.presentation.extra.launch
import lab.maxb.dark.presentation.extra.navigate
import lab.maxb.dark.presentation.extra.observe
import lab.maxb.dark.presentation.extra.setPasswordVisibility
import lab.maxb.dark.presentation.repository.network.oauth.google.GoogleSignInLogic
import lab.maxb.dark.presentation.viewModel.UserViewModel
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.sharedViewModel


class LoginFragment : Fragment(R.layout.login_fragment) {
    private val mViewModel: UserViewModel by sharedViewModel()
    private val mBinding: LoginFragmentBinding by dataBinding()
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

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mBinding.data = mViewModel
//        mGoogleSignInLogic.getAuthCode(requireActivity()).observeOnce(viewLifecycleOwner) { authCode ->
//            mViewModel.authorizeBySession(authCode).observeOnce(viewLifecycleOwner,
//                makeAuthResultHandler(null))
//        }

        mViewModel.showPassword.observe {
            mBinding.password.setPasswordVisibility(it)
        }
        mBinding.next.setOnClickListener {
            LoginFragmentDirections.actionLoginFragmentToSignupFragment().navigate()
        }
        mBinding.logIn.setOnClickListener {
            if (mViewModel.isLoading.value)
                return@setOnClickListener
            mViewModel.isLoading.value = true
            launch {
                mViewModel.authorize()
                mViewModel.profile.collectLatest { state ->
                    state.ifLoaded {
                        handleResult(R.string.incorrect_credentials_message, it)
                    }
                }
            }
        }

        mBinding.googleSignIn.setOnClickListener {
            mGoogleSignInPage.open(mGoogleSignInLogic.signInIntent)
        }
    }

    private fun onAuthWithGoogle(result: Intent?){
//        val credentials = result?.let { mGoogleSignInLogic.handleSignInResult(it) }
//        if (credentials == null)
//            onNotAuthorized(R.string.something_went_wrong)
//        else {
//            observeOnce(mViewModel.authorizeByOAUTHProvider(
//                credentials[0],
//                credentials[1],
//                credentials[2],
//            ), makeAuthResultHandler(R.string.something_went_wrong))
//        }
    }

    private fun handleResult(@StringRes message: Int?, profile: Profile?) {
        profile?.let {
            mViewModel.password.value = ""
            NavGraphDirections.actionGlobalMainFragment().navigate()
//            setFragmentResult(RESPONSE_LOGIN_SUCCESSFUL, bundleOf())
        } ?: onNotAuthorized(message)
    }

    private fun onNotAuthorized(@StringRes message: Int?) {
        message?.let { Toast.makeText(
            context, it, Toast.LENGTH_LONG
        ).show()}
        mViewModel.isLoading.value = false
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
        val GET_GOOGLE_ACCOUNT = unicname
        val RESPONSE_LOGIN_SUCCESSFUL = unicname
    }
}