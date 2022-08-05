package lab.maxb.dark.presentation.view

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.ActivityResultRegistry
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.StringRes
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.LifecycleOwner
import com.wada811.databinding.dataBinding
import lab.maxb.dark.NavGraphDirections
import lab.maxb.dark.R
import lab.maxb.dark.databinding.AuthFragmentBinding
import lab.maxb.dark.domain.model.Profile
import lab.maxb.dark.domain.operations.randomFieldKey
import lab.maxb.dark.presentation.extra.navigate
import lab.maxb.dark.presentation.extra.observe
import lab.maxb.dark.presentation.extra.setPasswordVisibility
import lab.maxb.dark.presentation.repository.network.oauth.google.GoogleSignInLogic
import lab.maxb.dark.presentation.viewModel.AuthViewModel
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.sharedViewModel


class AuthFragment : Fragment(R.layout.auth_fragment) {
    private val mViewModel: AuthViewModel by sharedViewModel()
    private val mBinding: AuthFragmentBinding by dataBinding()
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

        mViewModel.isAccountNew observe {
            mBinding.passwordRepeat.isVisible = it
        }

        mViewModel.showPassword observe {
            mBinding.password.setPasswordVisibility(it)
            mBinding.passwordRepeat.setPasswordVisibility(it)
        }

        mBinding.enter.setOnClickListener {
            if (mViewModel.isLoading.value)
                return@setOnClickListener

            when {
                mViewModel.hasEmptyFields() -> show(R.string.auth_message_hasEmptyFields)
                mViewModel.isPasswordsNotMatch() -> show(R.string.auth_message_passwordsNotMatch)
                else -> null
            }?.let { return@setOnClickListener }

            mViewModel.isLoading.value = true
            mViewModel.authorize()
        }

        mViewModel.profile observe { state ->
            if (!mViewModel.isLoading.value)
                return@observe
            
            state.ifLoaded {
                val message = if (mViewModel.isAccountNew.value)
                    R.string.auth_message_signup_incorrectCredentials
                else
                    R.string.auth_message_login_incorrectCredentials
                handleResult(message, it)
            }
        }
//        mBinding.googleSignIn.setOnClickListener {
//            mGoogleSignInPage.open(mGoogleSignInLogic.signInIntent)
//        }
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
            mViewModel.passwordRepeat.value = ""
            NavGraphDirections.navToMainFragment().navigate()
        } ?: onNotAuthorized(message)
    }

    private fun onNotAuthorized(@StringRes message: Int?) {
        message?.let { show(it) }
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

    private fun show(message: String) = Toast.makeText(
        context, message, Toast.LENGTH_LONG
    ).show()

    private fun show(message: Int) = show(getString(message))

    companion object {
        val GET_GOOGLE_ACCOUNT = randomFieldKey
    }
}