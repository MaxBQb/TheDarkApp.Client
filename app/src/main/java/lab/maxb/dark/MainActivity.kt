package lab.maxb.dark

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.appcompat.app.AppCompatDelegate.MODE_NIGHT_YES
import androidx.appcompat.widget.Toolbar
import androidx.core.view.isVisible
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupWithNavController
import lab.maxb.dark.Presentation.Extra.Delegates.viewBinding
import lab.maxb.dark.Presentation.Extra.launch
import lab.maxb.dark.Presentation.Extra.observe
import lab.maxb.dark.Presentation.ViewModel.UserViewModel
import lab.maxb.dark.databinding.MainActivityBinding
import org.koin.androidx.viewmodel.ext.android.viewModel


class MainActivity : AppCompatActivity(R.layout.main_activity) {
    val binding: MainActivityBinding by viewBinding()
    private val authViewModel: UserViewModel by viewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        AppCompatDelegate.setDefaultNightMode(MODE_NIGHT_YES)
        setSupportActionBar(binding.toolbar)
        val navController = (supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment).navController
        val appBarConfiguration = AppBarConfiguration(navController.graph)
        binding.toolbar.setupWithNavController(navController, appBarConfiguration)

        authViewModel.profile observe {
            it.ifLoaded { profile ->
                binding.toolbar.isVisible = profile != null
                if (profile == null && navController.currentDestination?.id != R.id.login_fragment)
                    navController.navigate(
                        NavGraphDirections.actionGlobalLoginFragment()
                    )
            }
        }
    }

    fun withToolbar(block: Toolbar.() -> Unit) = launch {
        binding.toolbar.apply(block)
    }
}