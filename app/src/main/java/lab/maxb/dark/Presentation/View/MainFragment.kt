package lab.maxb.dark.Presentation.View

import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import androidx.fragment.app.setFragmentResultListener
import androidx.navigation.NavOptions
import androidx.navigation.fragment.findNavController
import lab.maxb.dark.Presentation.Extra.Delegates.autoCleaned
import lab.maxb.dark.Presentation.ViewModel.UserViewModel
import lab.maxb.dark.R
import lab.maxb.dark.databinding.MainFragmentBinding
import org.koin.androidx.viewmodel.ext.android.sharedViewModel


class MainFragment : Fragment() {
    private val mViewModel: UserViewModel by sharedViewModel()
    private var mBinding: MainFragmentBinding by autoCleaned()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        mBinding = MainFragmentBinding.inflate(layoutInflater, container, false)
        return mBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        if (mViewModel.user.value == null) {
            openLoginView()
            return
        }

        setFragmentResultListener(LoginFragment.RESPONSE_LOGIN_SUCCESSFUL) {
            _, _ ->
            findNavController().run {
                navigate(
                    graph.startDestination,
                    null,
                    NavOptions.Builder()
                        .setPopUpTo(graph.startDestination, true)
                        .build()
                )
                navigate(R.id.RecognitionTaskList_fragment)
            }
        }

        mViewModel.user.observe(viewLifecycleOwner) { profile ->
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

    private fun openLoginView() = findNavController().navigate(R.id.login_fragment)
}