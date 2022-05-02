package lab.maxb.dark.Presentation.View

import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.setFragmentResultListener
import androidx.navigation.fragment.findNavController
import lab.maxb.dark.NavGraphDirections
import lab.maxb.dark.Presentation.Extra.Delegates.viewBinding
import lab.maxb.dark.Presentation.Extra.observe
import lab.maxb.dark.Presentation.ViewModel.UserViewModel
import lab.maxb.dark.R
import lab.maxb.dark.databinding.MainFragmentBinding
import org.koin.androidx.viewmodel.ext.android.sharedViewModel


class MainFragment : Fragment(R.layout.main_fragment) {
    private val mViewModel: UserViewModel by sharedViewModel()
    private val mBinding: MainFragmentBinding by viewBinding()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        (requireActivity() as AppCompatActivity)
            .supportActionBar
            ?.setHomeAsUpIndicator(null)

        if (mViewModel.user.value == null) {
            openLoginView()
            return
        }

        setFragmentResultListener(LoginFragment.RESPONSE_LOGIN_SUCCESSFUL) {
                _, _ ->
//            findNavController().navigate(
//                MainFragmentDirections.actionMainFragmentToRecognitionTaskListFragment()
//            )
        }

        observe(mViewModel.user) { profile ->
            if (profile == null) {
                openLoginView()
                return@observe
            }
            mBinding.welcomeLabel.text = getString(R.string.welcome_label,
                profile.user?.name ?: "Anonymous")
        }
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.signout_menu, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean
            = when (item.itemId) {
        R.id.sign_out -> {
            signOut()
            true
        }
        else -> super.onOptionsItemSelected(item)
    }

    private fun signOut() {
        mViewModel.signOut().invokeOnCompletion {
            openLoginView()
        }
    }

    private fun openLoginView() = findNavController().navigate(
        NavGraphDirections.actionGlobalLoginFragment()
    )
}