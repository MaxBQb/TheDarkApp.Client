package lab.maxb.dark.presentation.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.platform.ViewCompositionStrategy
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.fragment.app.Fragment
import lab.maxb.dark.NavGraphDirections
import lab.maxb.dark.R
import lab.maxb.dark.presentation.extra.navigate
import lab.maxb.dark.presentation.viewModel.AuthViewModel
import lab.maxb.dark.ui.theme.DarkAppTheme
import lab.maxb.dark.ui.theme.fontSize
import lab.maxb.dark.ui.theme.spacing
import org.koin.androidx.viewmodel.ext.android.sharedViewModel


class AuthHandleFragment : Fragment() {
    private val mViewModel: AuthViewModel by sharedViewModel()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ) = ComposeView(requireContext()).apply {
        setViewCompositionStrategy(ViewCompositionStrategy.DisposeOnViewTreeLifecycleDestroyed)
        setContent { AuthHandleRoot(
            mViewModel,
            onAuthorized = {
                NavGraphDirections.navToMainFragment().navigate()
            },
            onNotAuthorized = {
                mViewModel.handleNotAuthorizedYet()
                NavGraphDirections.navToAuthFragment().navigate()
            }
        ) }
    }
}

@Composable
fun AuthHandleRoot(viewModel: AuthViewModel,
                   onAuthorized: () -> Unit,
                   onNotAuthorized: () -> Unit) {
    AuthHandleRootStateless()

    val profile by viewModel.profile.collectAsState()
    profile.ifLoaded { it?.let { onAuthorized() } ?: onNotAuthorized() }
}

@Preview
@Composable
fun AuthHandleRootStateless() = DarkAppTheme { Surface {
    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(text = stringResource(id = R.string.handleAuth_pleaseWait),
            modifier = Modifier.padding(MaterialTheme.spacing.normal),
            fontSize = MaterialTheme.fontSize.normalHeader
        )
        LoadingCircle(0.8f, width = 12)
    }
} }