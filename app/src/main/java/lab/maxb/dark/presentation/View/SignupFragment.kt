package lab.maxb.dark.presentation.View

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.annotation.StringRes
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.wada811.databinding.dataBinding
import kotlinx.coroutines.flow.collectLatest
import lab.maxb.dark.Domain.Model.Profile
import lab.maxb.dark.NavGraphDirections
import lab.maxb.dark.R
import lab.maxb.dark.databinding.SignupFragmentBinding
import lab.maxb.dark.presentation.Extra.launch
import lab.maxb.dark.presentation.Extra.observe
import lab.maxb.dark.presentation.Extra.setPasswordVisibility
import lab.maxb.dark.presentation.view_model.UserViewModel
import org.koin.androidx.viewmodel.ext.android.sharedViewModel


class SignupFragment : Fragment(R.layout.signup_fragment) {
    private val mViewModel: UserViewModel by sharedViewModel()
    private val mBinding: SignupFragmentBinding by dataBinding()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mBinding.data = mViewModel

        mViewModel.showPassword.observe {
            mBinding.password.setPasswordVisibility(it)
            mBinding.passwordRepeat.setPasswordVisibility(it)
        }
        mBinding.back.setOnClickListener {
            findNavController().popBackStack()
        }
        mBinding.signup.setOnClickListener {
            if (mViewModel.isLoading.value)
                return@setOnClickListener
            mViewModel.isLoading.value = true
            launch {
                mViewModel.authorize(true)
                mViewModel.profile.collectLatest { state ->
                    state.ifLoaded {
                        handleResult(R.string.incorrect_signup_credentials_message, it)
                    }
                }
            }
        }
    }


    private fun handleResult(@StringRes message: Int?, profile: Profile?) {
        profile?.let {
            mViewModel.password.value = ""
            mViewModel.passwordRepeat.value = ""
            findNavController().navigate(
                NavGraphDirections.actionGlobalMainFragment()
            )
        } ?: onNotAuthorized(message)
    }

    private fun onNotAuthorized(@StringRes message: Int?) {
        message?.let { Toast.makeText(
            context, it, Toast.LENGTH_LONG
        ).show()}
        mViewModel.isLoading.value = false
    }
}