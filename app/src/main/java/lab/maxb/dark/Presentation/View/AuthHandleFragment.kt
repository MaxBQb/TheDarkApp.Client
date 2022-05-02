package lab.maxb.dark.Presentation.View

import android.os.Bundle
import android.view.View
import androidx.annotation.StringRes
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import lab.maxb.dark.Domain.Model.Profile
import lab.maxb.dark.MainActivity
import lab.maxb.dark.NavGraphDirections
import lab.maxb.dark.Presentation.Extra.Delegates.viewBinding
import lab.maxb.dark.Presentation.Extra.observeOnce
import lab.maxb.dark.Presentation.Extra.toggleVisibility
import lab.maxb.dark.Presentation.ViewModel.UserViewModel
import lab.maxb.dark.R
import lab.maxb.dark.databinding.AuthHandleFragmentBinding
import org.koin.androidx.viewmodel.ext.android.sharedViewModel


class AuthHandleFragment : Fragment(R.layout.auth_handle_fragment) {
    private val mViewModel: UserViewModel by sharedViewModel()
    private val mBinding: AuthHandleFragmentBinding by viewBinding()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        observeOnce(mViewModel.authorizeBySession(null), ::handleAuthState)
//        toggleToolbarVisibility(false)
    }

    private fun toggleToolbarVisibility(isVisible: Boolean) {
        (requireActivity() as MainActivity).binding
            .toolbar.isVisible = isVisible
    }

    private fun handleAuthState(profile: Profile?) = findNavController().navigate(
        if (profile == null)
            NavGraphDirections.actionGlobalLoginFragment()
        else
            AuthHandleFragmentDirections.actionAuthHandleFragmentToMainFragment()
    )
}