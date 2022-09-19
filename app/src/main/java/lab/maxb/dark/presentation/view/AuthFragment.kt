package lab.maxb.dark.presentation.view

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.annotation.StringRes
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import com.wada811.databinding.dataBinding
import lab.maxb.dark.NavGraphDirections
import lab.maxb.dark.R
import lab.maxb.dark.databinding.AuthFragmentBinding
import lab.maxb.dark.domain.model.Profile
import lab.maxb.dark.domain.operations.randomFieldKey
import lab.maxb.dark.presentation.extra.navigate
import lab.maxb.dark.presentation.extra.observe
import lab.maxb.dark.presentation.extra.setPasswordVisibility
import lab.maxb.dark.presentation.viewModel.AuthViewModel
import org.koin.androidx.viewmodel.ext.android.sharedViewModel


class AuthFragment : Fragment(R.layout.auth_fragment) {
    private val mViewModel: AuthViewModel by sharedViewModel()
    private val mBinding: AuthFragmentBinding by dataBinding()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mBinding.data = mViewModel

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

    private fun show(message: String) = Toast.makeText(
        context, message, Toast.LENGTH_LONG
    ).show()

    private fun show(message: Int) = show(getString(message))
}