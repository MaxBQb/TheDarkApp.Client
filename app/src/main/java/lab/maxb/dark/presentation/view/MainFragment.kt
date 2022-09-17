package lab.maxb.dark.presentation.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ExitToApp
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.platform.ViewCompositionStrategy
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.fragment.app.Fragment
import kotlinx.coroutines.launch
import lab.maxb.dark.R
import lab.maxb.dark.domain.model.isUser
import lab.maxb.dark.presentation.viewModel.AuthViewModel
import lab.maxb.dark.presentation.viewModel.utils.valueOrNull
import lab.maxb.dark.ui.theme.DarkAppTheme
import lab.maxb.dark.ui.theme.Golden
import lab.maxb.dark.ui.theme.fontSize
import lab.maxb.dark.ui.theme.spacing
import org.koin.androidx.viewmodel.ext.android.sharedViewModel


class MainFragment : Fragment() {
    private val mViewModel: AuthViewModel by sharedViewModel()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ) = ComposeView(requireContext()).apply {
        setViewCompositionStrategy(ViewCompositionStrategy.DisposeOnViewTreeLifecycleDestroyed)
        setContent { WelcomeRoot(mViewModel) }
    }
}


@Composable
fun WelcomeRoot(viewModel: AuthViewModel) = DarkAppTheme { Surface {
    val profile by viewModel.profile.collectAsState()
    val uiScope = rememberCoroutineScope()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(MaterialTheme.spacing.normal),
    ) {
        val user = profile.valueOrNull?.user
        Greeting(user?.name)
        if (profile.valueOrNull?.role?.isUser == true)
            UserRating(user?.rating ?: 0)

    }
    Exit(modifier = Modifier.wrapContentSize(Alignment.BottomCenter)) { uiScope.launch {
        viewModel.signOut()
    } }
} }

@Composable
fun Exit(modifier: Modifier = Modifier, onExit: () -> Unit) = Button(
    onClick = onExit,
    modifier = modifier.padding(MaterialTheme.spacing.normal)
) {
    Icon(
        Icons.Filled.ExitToApp,
        "",
        modifier=Modifier
            .size(ButtonDefaults.IconSize)
    )
    Spacer(modifier = Modifier.size(ButtonDefaults.IconSpacing))
    Text(text = stringResource(id = R.string.auth_menu_signOut))
}

@Composable
fun UserRating(rating: Int) {
    Text(
        buildAnnotatedString {
            append(stringResource(id = R.string.welcome_rating))
            append(": ")
            withStyle(SpanStyle(Golden)) {
                append(rating.toString())
            }
        },
        modifier = Modifier
            .fillMaxWidth()
    )
}


@Composable
fun Greeting(name: String?) {
    Text(
        name?.let {
            stringResource(id = R.string.welcome_welcome, it)
        } ?: stringResource(id = R.string.welcome_anonymousWelcome),
        fontSize = MaterialTheme.fontSize.normalHeader,
        modifier = Modifier.fillMaxWidth(),
        textAlign = TextAlign.Center
    )
}
