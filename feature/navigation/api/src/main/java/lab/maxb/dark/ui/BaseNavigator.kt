package lab.maxb.dark.ui

import androidx.compose.runtime.Composable
import androidx.navigation.NavController
import com.ramcosta.composedestinations.spec.DestinationSpec
import com.ramcosta.composedestinations.spec.Direction
import org.koin.compose.koinInject
import org.koin.core.parameter.parametersOf
import org.koin.core.qualifier.named

interface BaseNavigator {
    fun navigate(to: Direction)
    fun initialNavigate(from: DestinationSpec<*>, to: Direction)
    fun navigateUp()

    @get:Composable
    val currentDestination: DestinationSpec<*>
}

const val CORE_NAVIGATOR = "CORE"

@Composable
inline fun <reified T : BaseNavigator> NavController.asNavigator() = koinInject<BaseNavigator>(
    named(CORE_NAVIGATOR)
) {
    parametersOf(this)
} as T
