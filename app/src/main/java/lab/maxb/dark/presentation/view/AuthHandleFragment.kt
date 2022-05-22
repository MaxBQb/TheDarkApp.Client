package lab.maxb.dark.presentation.view

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import lab.maxb.dark.NavGraphDirections
import lab.maxb.dark.R
import lab.maxb.dark.databinding.AuthHandleFragmentBinding
import lab.maxb.dark.presentation.extra.delegates.viewBinding
import lab.maxb.dark.presentation.extra.navigate
import lab.maxb.dark.presentation.extra.observe
import lab.maxb.dark.presentation.viewModel.AuthViewModel
import org.koin.androidx.viewmodel.ext.android.sharedViewModel


class AuthHandleFragment : Fragment(R.layout.auth_handle_fragment) {
    private val mViewModel: AuthViewModel by sharedViewModel()
    private val mBinding: AuthHandleFragmentBinding by viewBinding()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mViewModel.profile observe {
            it.ifLoaded { profile ->
                if (profile == null)
                    NavGraphDirections.navToAuthFragment().navigate()
                else
                    NavGraphDirections.navToMainFragment().navigate()
            }
        }
    }
}