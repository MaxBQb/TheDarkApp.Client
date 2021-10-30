package lab.maxb.dark.Presentation.View

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import lab.maxb.dark.Presentation.Extra.Delegates.autoCleaned
import lab.maxb.dark.Presentation.ViewModel.LoginViewModel
import lab.maxb.dark.R
import lab.maxb.dark.databinding.LoginFragmentBinding
import org.koin.androidx.viewmodel.ext.android.viewModel

class LoginFragment : Fragment() {
    private val mViewModel: LoginViewModel by viewModel()
    private var mBinding: LoginFragmentBinding by autoCleaned()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        mBinding = LoginFragmentBinding.inflate(layoutInflater, container, false)
        mViewModel.authorizeBySession().observe(viewLifecycleOwner) { profile ->
            if (profile != null)
                onAuthorized()
            else
                mBinding.signIn.isEnabled = true
        }
        mBinding.signIn.setOnClickListener {
            mViewModel.authorize(
                mBinding.login.text.toString(),
                mBinding.password.text.toString(),
            ).observe(viewLifecycleOwner) { profile ->
                if (profile != null)
                    onAuthorized()
                else
                    Toast.makeText(context,
                        R.string.incorrect_credentials_message,
                        Toast.LENGTH_LONG
                    ).show()
            }
        }
        return mBinding.root
    }

    private fun onAuthorized() {
        mBinding.password.setText("")
        findNavController().navigate(
            LoginFragmentDirections.toRecognitionTaskListFragment(),
        )
    }
}